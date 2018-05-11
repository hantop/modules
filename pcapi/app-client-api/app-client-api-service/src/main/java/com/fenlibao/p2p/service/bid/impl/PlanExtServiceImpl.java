package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.dao.SendSmsRecordExtDao;
import com.fenlibao.p2p.dao.coupon.CouponDao;
import com.fenlibao.p2p.dao.plan.PlanInfoDao;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.entity.*;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.bid.PlanBidVO;
import com.fenlibao.p2p.service.bid.IBidDmService;
import com.fenlibao.p2p.service.bid.PlanExtService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.bid.migrate.impl.TenderOrderExecutor;
import com.fenlibao.p2p.service.plan.UserPlanRepayService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计划出借辅助类
 * Created by LouisWang on 2015/8/14.
 */
@Service
public class PlanExtServiceImpl implements PlanExtService {
    private static final Logger logger = LogManager.getLogger(PlanExtServiceImpl.class);
    @Resource
    private PrivateMessageService privateMessageService;
    @Resource
    private IBidDmService bidDmService;
    @Resource
    private TenderOrderExecutor tenderOrderExecutor;

    @Resource
    private SendSmsRecordDao sendSmsRecordDao;
    @Resource
    private SendSmsRecordExtDao sendSmsRecordExtDao;
    @Resource
    private RedpacketService redpacketService;
    @Resource
    private CouponDao couponDao;
    @Resource
    private ITradeDao tradeDao;
    @Resource
    private PlanInfoDao planInfoDao;
    @Resource
    private UserPlanRepayService userPlanRepayService;
    @Resource
    private PlanService planService;

    /**
     * 出借单个标
     *
     * @param planBidVO
     * @return
     */
    @Override
    public int doBidForPlan(Connection connection, PlanBidVO planBidVO, int accountId) throws Throwable {
        Savepoint savepoint = connection.setSavepoint();
        try {
            Map<String, String> rtnMap = bidDmService.doBid(connection, planBidVO.getBidId(), planBidVO.getPurchaseAmount(), accountId, null, null, true);
            //余额投标订单
            Throwable throwable1 = tenderOrderExecutor.submitKernel(connection, IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
            if (throwable1 != null) {
                tenderOrderExecutor.log(connection, IntegerParser.parse(rtnMap.get("orderId")), throwable1);
            }
            //确认订单
            Throwable throwable2 = tenderOrderExecutor.confirmKernel(connection, IntegerParser.parse(rtnMap.get("orderId")), rtnMap, false, false);
            if (throwable2 != null) {
                tenderOrderExecutor.log(connection, IntegerParser.parse(rtnMap.get("orderId")), throwable2);
            }
            // 订单详情
            InverstBidTradeInfo inverstBidTradeInfo = tenderOrderExecutor.getBidOrderDetail(connection, IntegerParser.parse(rtnMap.get("orderId")));
            return inverstBidTradeInfo.getBidRecordId();
        } catch (Throwable e) {
            connection.rollback(savepoint);
            throw e;
        }
    }

    /**
     * 发送短信和站内信
     *
     * @param phoneNum
     * @param userId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void sendSmsAndLetter(String phoneNum, String userId, String content, VersionTypeEnum versionTypeEnum) {
        String znxSuffixContent = Sender.get("znx.suffix.content");
        //发短信
        try {
            sendMsg(phoneNum, content);
        } catch (Throwable t) {
            logger.error("发送短信失败", t);
        }
        //站内信
        try {
            privateMessageService.sendLetter(userId, InterfaceConst.PRIVATEMESSAGE_TITL_TZTZ, content + znxSuffixContent, versionTypeEnum);
        } catch (Throwable t) {
            logger.error("发送站内信失败", t);
        }
    }

    /**
     * 发送短信
     *
     * @param phoneNum
     * @param content
     */
    @Override
    public void sendMsg(String phoneNum, String content) {
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

    /**
     * 使用红包
     *
     * @param planId
     * @param planRecordId
     * @param userInfo
     * @param fxhbIds
     */
    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void useRedPackets(Integer orderId, List<UserRedPacketInfo> userRedPacketInfos, int planId, int planRecordId, UserInfo userInfo, String fxhbIds, String investType, VersionTypeEnum versionTypeEnum) throws Exception {
        try {
            //更新标和红包的状态(计划)
            for (UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                Map<String, Object> tmpParam = new HashMap<String, Object>();
                tmpParam.put("status", InterfaceConst.FXHB_TRADE_STATUS);
                tmpParam.put("fxhbId", userRedPacketInfo.getId());
                tmpParam.put("investType", investType);
                if (InterfaceConst.FXHB_INVEST_TYPE_CUSPLAN.equals(investType)) {
                    tmpParam.put("planId", planId);
                    tmpParam.put("planRecordId", planRecordId);
                }
                if (InterfaceConst.FXHB_INVEST_TYPE_INVESTPLAN.equals(investType)) {
                    tmpParam.put("userPlanId", planRecordId);
                }
                redpacketService.updateRedpacketsRelationForPlan(tmpParam);
            }
            //消费返现红包
            redpacketService.grantRedPackets(orderId, userRedPacketInfos, String.valueOf(userInfo.getUserId()), userInfo.getPhone(), FeeCode.REGISTERRETURNCACH_REDPACKET, versionTypeEnum);
        } catch (Exception e) {
            e.printStackTrace();
            String message = "[出借计划时返现红包使用失败：]";
            logger.error(message, e);
            // 申购时返现红包发放失败
            HashMap<String, Object> errMap = new HashMap<>();
            errMap.put("userId", userInfo.getUserId());
            errMap.put("userRedpacketId", fxhbIds);
//            if (inverstBidTradeInfo != null) {
//                errMap.put("bidId", inverstBidTradeInfo.getBidId());
//            }
            errMap.put("message", message);
            errMap.put("redType", FeeCode.REGISTERRETURNCACH_REDPACKET);
            try {
                redpacketService.recordRedpackExceptionLog(errMap);
            } catch (Exception e1) {
                logger.error(errMap.toString(), e1);
            }
            throw e;
        }
    }

    /**
     * 使用加息券
     *
     * @param planRecordId
     * @param userInfo
     * @param jxqId
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void useCoupon(int planRecordId, UserInfo userInfo, String jxqId, String investType) throws Exception {
        try {
            Map<String, Object> paramMap = new HashMap<>(1);
            paramMap.put("planRecordId", planRecordId);
            paramMap.put("userCouponId", jxqId);
            paramMap.put("userId", userInfo.getUserId());
            paramMap.put("investType", investType);
            if (InterfaceConst.FXHB_INVEST_TYPE_CUSPLAN.equals(investType)) {
                int resExt = couponDao.insertUserCouponExt(paramMap);
                if (resExt < 1) {
                    String msg = String.format("[出借计划时加息卷更新信息失败：],加息卷id:%s,planRecordId:%s"
                            , jxqId, planRecordId);
                    logger.error(msg);
                    throw new RuntimeException(msg);
                }
            }
            int result = couponDao.updateUserCouponForPlan(paramMap);
            if (result < 1) {
                String msg = String.format("[出借计划时加息卷更新信息失败：],加息卷id:%s,planRecordId:%s"
                        , jxqId, planRecordId);
                logger.error(msg);
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            logger.error(String.format("[出借计划时加息卷使用异常：],加息卷id:%s,planRecordId:%s"
                    , jxqId, planRecordId), e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void updateInvestPlanStatus(int planId) throws Exception {
        //检查封计划,更新状态
        int res = tradeDao.updateInvestPlanStatus(planId);
        if (res > 0) {
            int operatorId = planService.getPmsUserId();
            planInfoDao.insertPlanTermination(planId, operatorId);
            userPlanRepayService.createExpectedRepayPlan(planId);
        }
    }
}