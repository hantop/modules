package com.fenlibao.p2p.service.sms.impl;

import com.dimeng.framework.message.sms.entity.SmsTask;
import com.fenlibao.p2p.dao.CheckErrorDao;
import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.dao.SmsValidCodeDao;
import com.fenlibao.p2p.model.entity.CheckError;
import com.fenlibao.p2p.model.entity.SmsValidcode;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.base.db.DbPoolConnection;
import com.fenlibao.p2p.service.sms.SmsExtracterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

@Service
public class SmsExtracterServiceImpl implements SmsExtracterService {

    @Resource
    private SmsValidCodeDao smsValidCodeDao;
    @Resource
    private CheckErrorDao checkErrorDao;
    @Resource
    private SendSmsRecordDao sendSmsDao;

    @Override
    public SmsTask[] extract(int maxCount, int expiresMinutes) throws Throwable {
        if (maxCount <= 0) {
            return null;
        }
        if (expiresMinutes <= 0) {
            expiresMinutes = 15;
        }
        List<SmsTask> sendTasks = null;
        try (Connection connection = DbPoolConnection.getInstance().getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT M.F01,M.F02,M.F03,GROUP_CONCAT(N.F02) FROM s10._1040 M,s10._1041 N WHERE M.F01 = N.F01 AND M.F05= ? GROUP BY M.F01 ORDER BY M.F04 ASC LIMIT 0,?")) {
                ps.setString(1, "W");
                ps.setInt(2, maxCount);
                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    if (sendTasks == null) {
                        sendTasks = new ArrayList<SmsTask>();
                    }
                    SmsTask sendTask = new SmsTask();
                    sendTask.id = resultSet.getLong(1);
                    sendTask.type = resultSet.getInt(2);
                    sendTask.content = resultSet.getString(3);
                    String recs = resultSet.getString(4);
                    if ((null != recs) && (!"".equals(recs.trim()))) {
                        sendTask.receivers = recs.split(",");

                        sendTasks.add(sendTask);
                    }
                }
            }

            if (sendTasks == null) {
                return null;
            }
            connection.setAutoCommit(false);
            try (PreparedStatement psu = connection.prepareStatement("UPDATE s10._1040 SET F05=?,F06=? WHERE F01=?")) {
                long l = System.currentTimeMillis();
                for (SmsTask sendTask : sendTasks)
                    if (sendTask != null) {
                        psu.setString(1, "Z");
                        psu.setTimestamp(2, new Timestamp(l + expiresMinutes * 60 * 1000));
                        psu.setLong(3, sendTask.id);
                        psu.addBatch();
                    }
                psu.executeBatch();
            }
            connection.commit();
            connection.setAutoCommit(true);

            return sendTasks == null ? null : (SmsTask[]) sendTasks.toArray(new SmsTask[sendTasks.size()]);
        }

    }

    @Override
    public void mark(long id, boolean success, String extra) throws Throwable {
        if (id <= 0) {
            return;
        }
        try (Connection connection = DbPoolConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);// 打开事务
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO s10._1042(F01,F02,F03,F04,F05,F06) SELECT F01,F02,F03,F04,?,? FROM s10._1040 WHERE F01=?");

            if (success)
                ps.setString(1, "YES");
            else {
                ps.setString(1, "NO");
            }
            ps.setString(2, extra);
            ps.setLong(3, id);
            ps.execute();

            PreparedStatement ps2 = connection.prepareStatement("UPDATE s10._1040 set F05 = 'Y' WHERE F01=?");
            ps2.setLong(1, id);
            ps2.execute();

            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void captchaValidate(String phoneNum, String captcha, String type) throws Exception {
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("phoneNum", phoneNum);
        map.put("type", type);
        List<SmsValidcode> list = smsValidCodeDao.getCode(map);
        if (null != list && list.size() > 0) {
            SmsValidcode code = list.get(0);
            long outTime = code.getOutTime().getTime();
            if (outTime < System.currentTimeMillis()) {
                throw new BusinessException(ResponseCode.COMMON_CAPTCHA_TIMEOUT);
            } else if (!code.getValidCode().equals(captcha)) {
                //添加验证错误记录
                CheckError checkError = new CheckError();
                checkError.setSendType(1);
                checkError.setSendTo(phoneNum);
                checkError.setVeryCode(captcha);
                checkError.setCrateTime(new Date());
                checkErrorDao.insertMatchVerifyCodeError(checkError); //方法外不需要事务，后面异常不影响提交
                throw new BusinessException(ResponseCode.COMMON_CAPTCHA_INVALID);
            }
        } else {
            throw new BusinessException(ResponseCode.COMMON_CAPTCHA_INVALID);
        }
    }

    @Override
    public void sendMsg(String mobile, String content, int type) throws Exception {
        Integer msgId = sendSmsDao.insertT1040(type, content);
        if (msgId != null) {
            if (msgId > 0) {
                sendSmsDao.insertT1041(msgId, mobile);
            }
        }
    }
}
