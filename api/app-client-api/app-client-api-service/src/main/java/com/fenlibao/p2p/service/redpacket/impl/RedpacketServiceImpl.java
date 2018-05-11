package com.fenlibao.p2p.service.redpacket.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.dao.SendSmsRecordExtDao;
import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.dao.redpacket.RedpacketDao;
import com.fenlibao.p2p.model.entity.*;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.BidTypeEnum;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.bidinfo.BidTypeVO;
import com.fenlibao.p2p.model.vo.redpacket._RedPacketVO;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.xinwang.trade.XWSyncTransactionService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Administrator on 2015/10/16.
 */
@Service
public class RedpacketServiceImpl implements RedpacketService {
    private static final Logger logger= LogManager.getLogger(RedpacketServiceImpl.class);
    @Resource
    private RedpacketDao redpacketDao;
    @Resource
    private PrivateMessageService privateMessageService;
    @Resource
    private SendSmsRecordDao sendSmsRecordDao;
    @Resource
    private SendSmsRecordExtDao sendSmsRecordExtDao;
    @Resource
    private BidInfoDao bidInfoDao;
    @Resource
    private XWSyncTransactionService xwSyncTransactionService;

    private void _addUserRedpackets(String userId, List<UserRedPacketInfo> userRedPacketInfos) {
        if(userRedPacketInfos != null && userRedPacketInfos.size() > 0) {
            // 红包金额
            BigDecimal hbBalance;
            // 用户红包有效时间
            Calendar calendar;
            // 红包有效期
            String dateStr;
            // 增加用户的红包
            for(UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                hbBalance = userRedPacketInfo.getHbBalance();
                calendar = buildUserRedpacketValidTime(userRedPacketInfo.getEffectDay());
                dateStr = DateUtil.getDateTime(new Timestamp(calendar.getTimeInMillis()));
                addUserRedpacket(userRedPacketInfo.getHbId(), hbBalance, userId, dateStr);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BigDecimal grantRedpacketFirstLogin(String phoneNum, String userId) throws Exception {
        // 奖励总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 红包类型：注册返现红包
        int redpacketTypeReturncach = InterfaceConst.REDPACKET_REGISTERRETURNCACH;
        List<UserRedPacketInfo> userRedPacketInfos = null;
        // 增加用户的返现红包
        try {
            // 获取注册红包设置信息
            userRedPacketInfos = this.getActivityRedBagByType(redpacketTypeReturncach);
            // 红包金额
            BigDecimal hbBalance;
            if(userRedPacketInfos != null && userRedPacketInfos.size() > 0) {
                // 增加用户的红包
                _addUserRedpackets(userId, userRedPacketInfos);
                // 计算红包总额
                for(UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                    hbBalance = userRedPacketInfo.getHbBalance();
                    totalAmount = totalAmount.add(hbBalance);
                }
            }
        } catch (Exception e) {
            String message = "[首次登陆发放注册返现红包异常：]" + e.getMessage();
            logger.error(message, e);
            StringBuffer stringBuff = new StringBuffer();
            for (int i = 0; i < userRedPacketInfos.size() ; i++) {
                stringBuff.append(userRedPacketInfos.get(i).getHbId()).append("|");
            }
            HashMap<String, Object> errMap = new HashMap<>();
            errMap.put("userId", userId);
            errMap.put("redPacketId", stringBuff.toString());
            errMap.put("userRedpacketId", null);
            errMap.put("bidId",null);
            errMap.put("message", message);
            errMap.put("redType", FeeCode.CACH_REDPACKET);
            try {
                this.recordRedpackExceptionLog(errMap);
            } catch (Exception e1) {
                logger.error("[首次登陆发放注册返现红包插入参数:]" + errMap.toString(), e1);
            }
            throw new Exception();
        }
        return totalAmount;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BigDecimal grantRedpacketRegister(String phoneNum, String userId) throws Exception {
        // 奖励总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 红包类型：注册返现红包
        int redpacketTypeReturncach = InterfaceConst.REDPACKET_REGISTERRETURNCACH;
        List<UserRedPacketInfo> userRedPacketInfos = null;
        // 增加用户的返现红包
        try {
            // 获取注册红包设置信息
            userRedPacketInfos = this.getActivityRedBagByType(redpacketTypeReturncach);
            // 红包金额
            BigDecimal hbBalance;
            if(userRedPacketInfos != null && userRedPacketInfos.size() > 0) {
                // 增加用户的红包
                _addUserRedpackets(userId, userRedPacketInfos);
                // 计算红包总额
                for(UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                    hbBalance = userRedPacketInfo.getHbBalance();
                    totalAmount = totalAmount.add(hbBalance);
                }
            }
        } catch (Exception e) {
            String message = "[注册返现红包异常：]" + e.getMessage();
            logger.error(message, e);
            StringBuffer stringBuff = new StringBuffer();
            for (int i = 0; i < userRedPacketInfos.size() ; i++) {
                stringBuff.append(userRedPacketInfos.get(i).getHbId()).append("|");
            }
            HashMap<String, Object> errMap = new HashMap<>();
            errMap.put("userId", userId);
            errMap.put("redPacketId", stringBuff.toString());
            errMap.put("userRedpacketId", null);
            errMap.put("bidId",null);
            errMap.put("message", message);
            errMap.put("redType", FeeCode.CACH_REDPACKET);
            try {
                this.recordRedpackExceptionLog(errMap);
            } catch (Exception e1) {
                logger.error("[注册返现红包插入参数:]" + errMap.toString(), e1);
            }
            throw new Exception();
        }
        return totalAmount;
    }

    @Transactional(readOnly = false)
    @Override
    public void recordRedpackExceptionLog(Map<String, Object> paramsMap) {
        // 新增红包发放异常日志
        redpacketDao.addRedpackExceptionLog(paramsMap);
    }

    @Override
    public int addTruninFundsRecord(int truninAccountId, int trunoutAccountId, int tradeTypeId, BigDecimal payinAmount, BigDecimal balance, String remark) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("truninAccountId", truninAccountId);
        paramMap.put("trunoutAccountId", trunoutAccountId);
        paramMap.put("tradeTypeId", tradeTypeId);
        paramMap.put("payinAmount", payinAmount);
        paramMap.put("balance", balance);
        paramMap.put("remark", remark);
        return redpacketDao.addTruninFundsRecord(paramMap);
    }

    @Override
    public int addTrunoutFundsRecord(int truninAccountId, int trunoutAccountId, int tradeTypeId, BigDecimal payoutAmount, BigDecimal balance, String remark) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("truninAccountId", truninAccountId);
        paramMap.put("trunoutAccountId", trunoutAccountId);
        paramMap.put("tradeTypeId", tradeTypeId);
        paramMap.put("payoutAmount", payoutAmount);
        paramMap.put("balance", balance);
        paramMap.put("remark", remark);
        return redpacketDao.addTrunoutFundsRecord(paramMap);
    }

    @Override
    public void grantRedPacket(String userId, BigDecimal amount, FeeType feeType){
        // 锁定并获取用户往来账户
        String accountTypeWlzh = InterfaceConst.ACCOUNT_TYPE_WLZH;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("accountType", accountTypeWlzh);
        UserAccount userAccount = redpacketDao.lockUserAccount(paramMap);
        if(userAccount == null) {
            throw new LogicalException("用户资金账户不存在");
        }
        // 用户往来账户ID
        int accountId = userAccount.getAccountId();
        // 发红包的账户名称
        String salayKey = Config.get("redpacket.grantAccount");
        paramMap.clear();
        paramMap.put("accountName", salayKey);
        paramMap.put("accountType", accountTypeWlzh);
        // 查询资金账户ID
        Integer grantAccountId = redpacketDao.getAccountId(paramMap);
        if(grantAccountId == null) {
            throw new LogicalException("发放红包资金账户不存在");
        }
        // 锁定资金账户
        UserAccount salaryaccount = redpacketDao.lockSalaryaccount(grantAccountId);
        int cachRedpacket = feeType.getCode();
        String cachRedpacketName = feeType.getName();
        // 平台账户ID
        int ptzhId = salaryaccount.getAccountId();
        // 平台账户余额
        BigDecimal ptAmount = salaryaccount.getBalance();
        // 插入个人资金交易记录，内部转账(转入)
        addTruninFundsRecord(accountId, ptzhId, cachRedpacket, amount, userAccount.getBalance().add(amount), cachRedpacketName);
        // 插入平台资金交易记录，内部转账(转出)
        addTrunoutFundsRecord(ptzhId, accountId, cachRedpacket, amount, ptAmount.subtract(amount), cachRedpacketName);
        // 增加用户资金余额
        increaseAccountAmount(amount, userAccount);
        // 减少发放红包资金账户WLZH金额
        subtractAccountAmount(amount, ptzhId);
    }

    private void sendMsg(String phoneNum, String content) {
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//验证码过期时间30分钟
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


    private void subtractAccountAmount(BigDecimal amount, int ptzhId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accountId", ptzhId);
        paramMap.put("amount", amount);
        redpacketDao.subtractUserAccountAmount(paramMap);
    }

    private void increaseAccountAmount(BigDecimal amount, UserAccount userAccount) {
        Map<String, Object> paramMap = new HashMap<>();
        // 增加用户资金余额
        int accountId = userAccount.getAccountId();
        paramMap.put("accountId", accountId);
        paramMap.put("amount", amount);
        redpacketDao.increaseUserAccountAmount(paramMap);
    }

    //@Transactional(readOnly = false)
    @Override
    public void grantRedPackets(int orderId, List<UserRedPacketInfo> userRedPacketInfos, String userId, String phoneNum, int tradeType, VersionTypeEnum versionTypeEnum) throws Exception {
        // 根据交易类型编码获取交易类型
        // 现金红包编码
        FeeType feeType = getFeeType(tradeType);
        String smsPattern = "";
        //现金红包类型
        if(FeeCode.CACH_REDPACKET == tradeType){
            // 消息模板
            smsPattern = Sender.get("sms.register.xjhb.content");
        }else if(FeeCode.REGISTERRETURNCACH_REDPACKET == tradeType){    //返现红包
            smsPattern = Sender.get("sms.fxhb.content");
        }
        if(VersionTypeEnum.CG.equals(versionTypeEnum)){
            //调用新网接口
            List<BigDecimal> amountList = new ArrayList<>();
            for(UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                amountList.add(userRedPacketInfo.getHbBalance());
            }
            BusinessType businessType = new BusinessType();
            BeanUtils.copyProperties(feeType, businessType);
            xwSyncTransactionService.syncTransactionMarketing(Integer.parseInt(userId), UserRole.INVESTOR.getCode(), amountList, businessType, orderId);
            // 发放用户的现金红包
            for(UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                try{
                    BigDecimal hbBalance = userRedPacketInfo.getHbBalance();
                    // 发送站内信
                    String content = smsPattern.replace("#{amount}", hbBalance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    String znxSuffixContent = Sender.get("znx.suffix.content");
                    privateMessageService.sendLetter(userId, InterfaceConst.PRIVATEMESSAGE_TITLE, content+znxSuffixContent, VersionTypeEnum.CG);
                    // 发送短信
                    sendMsg(phoneNum, content);
                }catch (Exception e){
                    logger.error(e, e);
                }
            }
        }else{
            // 发放用户的现金红包
            for(UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                BigDecimal hbBalance = userRedPacketInfo.getHbBalance();
                grantRedPacket(userId, hbBalance, feeType);
                // 发送站内信
                String content = smsPattern.replace("#{amount}", hbBalance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                String znxSuffixContent = Sender.get("znx.suffix.content");
                privateMessageService.sendLetter(userId, InterfaceConst.PRIVATEMESSAGE_TITLE, content+znxSuffixContent, VersionTypeEnum.PT);
                // 发送短信
                sendMsg(phoneNum, content);
            }
        }
    }

//    /**
//     * 更新内部转账明细状态
//     * @param status 转账状态
//     * @param transferId 转账发放明细ID
//     */
//    private void udpateTransferGrant(String status, int transferId) {
//    }

    /**
     * 根据交易类型编码获取交易类型
     * @param code
     * @return
     */
    private FeeType getFeeType(int code) {
        return redpacketDao.getFeeType(code);
    }

    @Override
    public int getRedpacketCount(Map<String, Object> paramMap) {
        return redpacketDao.getRedpacketCount(paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getRedpackets(Map<String, Object> paramMap) {
        List<UserRedPacketInfo> userRedPacketInfos = redpacketDao.getRedpackets(paramMap);
        List outRedPacketInfos = new ArrayList();
        Map<String,Object> redPacketMap;
        List<BidTypeVO> bidTypes;
        StringBuffer bidTypeNames;
        for (UserRedPacketInfo info : userRedPacketInfos) {
            redPacketMap = new HashMap<>(9);
            redPacketMap.put("hbId",info.getId());
            redPacketMap.put("type",1);
            redPacketMap.put("status",paramMap.get("status"));
            redPacketMap.put("timestamp",info.getTimestamp());
            redPacketMap.put("hbBalance",info.getHbBalance().intValue());
            redPacketMap.put("conditionBalance", info.getConditionBalance().intValue());
            redPacketMap.put("deadlineTips", info.getInvestDeadline() != null ? String.format(InterfaceConst.INVESTMENT_DEADLINE_TIPS, info.getInvestDeadline()) : InterfaceConst.INVESTMENT_DEADLINE_TIPS_ALL);
            bidTypes = info.getBidTypes();
            if (bidTypes == null || bidTypes.size() == 0 || bidTypes.size() == BidTypeEnum.length()) {
                redPacketMap.put("bidTypeTips", InterfaceConst.INVESTMENT_BIDTYPE_TIPS_ALL);
            } else {
                bidTypeNames = new StringBuffer(4);
                for (BidTypeVO type : bidTypes) {
                    bidTypeNames.append(type.getTypeName() + "、");
                }
                bidTypeNames.deleteCharAt(bidTypeNames.length() - 1);
                redPacketMap.put("bidTypeTips", String.format(InterfaceConst.INVESTMENT_BIDTYPE_TIPS, bidTypeNames));
            }

            long dayGap = info.getTimestamp().getTime() - DateUtil.nowDate().getTime();
            double millisecondAday = 1000*60*60*24;
            redPacketMap.put("overdueFlag", (dayGap/millisecondAday >= 0 && dayGap/millisecondAday <= InterfaceConst.OVERDUE));

            outRedPacketInfos.add(redPacketMap);
        }
        return outRedPacketInfos;
    }

    @Override
    public List<UserRedPacketInfo> getActivityRedBagByType(int redpacketType) {
        Map<String, Object> redParamMap = new HashMap<>();
        // 当前时间
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        // 注册返现红包
        redParamMap.put("type", redpacketType);
        redParamMap.put("currentTime", currentTimestamp);
        return redpacketDao.getActivityRedBagByType(redParamMap);
    }

    @Override
    public void addUserRedpacket(int redPacketId, BigDecimal amount, String userId, String validTime) {
        // 红包状态(未使用)
        int redpacketStatusNoused = InterfaceConst.REDPACKET_STATUS_NOUSED;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("validTime", validTime);
        paramMap.put("status", redpacketStatusNoused);
        paramMap.put("redPacketId", redPacketId);
        paramMap.put("userId", userId);
        paramMap.put("grantStatus", 1);
        redpacketDao.addUserRedpacket(paramMap);
    }

    @Transactional(readOnly = false)
    @Override
    public void addUserRedpackets(List<UserRedPacketInfo> userRedPacketInfos, String userId, String phoneNum,
                                  String smsTemplate, String letterSuffix, boolean isSendSms) throws Exception {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalNum = 0;
        String validTimeFormat = "";
        // 增加用户的红包
        for(UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
            // 红包金额
            BigDecimal hbBalance = userRedPacketInfo.getHbBalance();
            // 用户红包有效时间
            Calendar calendar = buildUserRedpacketValidTime(userRedPacketInfo.getEffectDay());
            // 红包有效期
            Timestamp validTime = new Timestamp(calendar.getTimeInMillis());
            String dateStr = DateUtil.getDateTime(validTime);
            addUserRedpacket(userRedPacketInfo.getHbId(), hbBalance, userId, dateStr);
            // 格式化日期时间
            validTimeFormat = DateUtil.getDate(new Date(validTime.getTime()));

            totalAmount=totalAmount.add(hbBalance);
            totalNum++;
        }
        // 发送短信
        if (StringUtils.isNotBlank(smsTemplate)) {
            String content = smsTemplate
                    .replace("#{totalNum}", String.valueOf(totalNum))
                    .replace("#{amount}", totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                    .replace("#{validTime}", validTimeFormat);
            if (isSendSms) {
                sendMsg(phoneNum, content);
            }
            // 发送站内信
            if (StringUtils.isNotBlank(letterSuffix)) {
                privateMessageService.sendLetter(userId, InterfaceConst.PRIVATEMESSAGE_TITLE, content + letterSuffix, VersionTypeEnum.PT);
            }
        }

    }

    /**
     * @author wangyunjing
     * @date 20151026
     * @todo 投标成功后获取的返现红包
     * @param paramMap
     */
    public List<UserRedPacketInfo> getBidRedpacket(Map<String,Object> paramMap) throws Exception{
        return redpacketDao.getBidRedpacket(paramMap);
    }

    /**
     * @author wangyunjing
     * @date 20151026
     * @todo 投标成功后获取的返现红包
     * @param paramMap
     */
    //@Transactional(readOnly = false)
    @Override
    public void updateRedpacketsRelation(Map<String, Object> paramMap) {
        redpacketDao.updateRedpacketsRelation(paramMap);
    }

    /**
     * 投资计划成功后使用返现红包 修改状态
     * @param paramMap
     */
    @Override
    public void updateRedpacketsRelationForPlan(Map<String, Object> paramMap) {
        redpacketDao.updateRedpacketsRelationForPlan(paramMap);
    }

    @Override
    public InverstBidTradeInfo getBidOrderDetail(Integer orderId) {
           return redpacketDao.getBidOrderDetail(orderId);
    }

    @Override
    public _RedPacketVO getById(int id) {
        return redpacketDao.getById(id);
    }

    /**
     * 用户红包有效时间
     * @param effectDay
     * @return
     */
    private Calendar buildUserRedpacketValidTime(Integer effectDay) {
        Calendar calendar = Calendar.getInstance();
        // 用户红包有效时间为当前时间加上红包有效天数
        calendar.add(Calendar.DATE, effectDay);
        // 时间到23:59:59
        calendar.set(calendar.HOUR_OF_DAY, 23);
        calendar.set(calendar.MINUTE, 59);
        calendar.set(calendar.SECOND, 59);
        return calendar;
    }

    @Override
    public List<UserRedPacketInfo> getActivityRedBagBySetting(Map<String, Object> paramMap) {
        return redpacketDao.getActivityRedBagBySetting(paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getActivityRedBagList(Map<String, Object> paramMap) {
        return redpacketDao.getActivityRedBagList(paramMap);
    }

    @Override
    public List<UserRedPacketInfo> getUserRedBagByActivity(Map<String, Object> paramMap) {
        return redpacketDao.getUserRedBagByActivity(paramMap);
    }

}
