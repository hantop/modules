package com.fenlibao.p2p.service.impl;

import com.fenlibao.p2p.dao.*;
import com.fenlibao.p2p.model.entity.*;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.AdImageVo;
import com.fenlibao.p2p.model.vo.ApkUpdateVo;
import com.fenlibao.p2p.service.DeviceService;
import com.fenlibao.p2p.service.SpecialAccountService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

@Service
public class SpecialAccountServiceImpl implements SpecialAccountService {

    private static final Logger logger = LogManager.getLogger(SpecialAccountServiceImpl.class);

    @Resource
    private SpecialAccountDao specialAccountDao;

    @Resource
    private SendSmsRecordDao sendSmsRecordDao;

    @Resource
    private SendSmsRecordExtDao sendSmsRecordExtDao;

    @Override
    public List<SpecialAccount> getSpecialPhone(String type) {
        return specialAccountDao.getSpecialPhone(type);
    }
    @Override
    @Transactional
    public void sendMessage(String phoneNum,String content) throws Exception{
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//过期时间30分钟
        SendSmsRecord record = new SendSmsRecord();
        record.setContent(content);
        record.setCreateTime(new Date());
        record.setStatus("W");
        record.setType(0);
        record.setOutTime(outTime);
        sendSmsRecordDao.insertSendSmsRecord(record);

        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phoneNum);
        sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
    }

    @Override
    public void updateSpecialAccountSendCount(SpecialAccount specialAccount) {
       this.specialAccountDao.updateSpecialAccountSendCount(specialAccount);
    }
}
