package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.coupon.CouponManageDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.consts.HXConst;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.consts.TradeConsts;
import com.fenlibao.p2p.model.trade.entity.*;
import com.fenlibao.p2p.model.trade.entity.bid.T6504;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.enums.T6230_F15;
import com.fenlibao.p2p.model.trade.enums.T6230_F20;
import com.fenlibao.p2p.model.trade.enums.T6250_F07;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.coupon.UserCouponState;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.trade.vo.UserCouponVO;
import com.fenlibao.p2p.model.trade.vo.UserRedpacketVO;
import com.fenlibao.p2p.model.user.entity.AssetAccount;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.enums.T6101_F03;
import com.fenlibao.p2p.model.user.enums.UserResponseCode;
import com.fenlibao.p2p.model.user.exception.UserException;
import com.fenlibao.p2p.service.dm.hx.busi.HXCommonService;
import com.fenlibao.p2p.service.dm.hx.busi.HXTenderService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.coupon.CouponManageService;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import org.aeonbits.owner.ConfigCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 投标
 * Created by zcai on 2016/12/8.
 */
@Service
public class HXTenderServiceImpl extends HXOrderProcessImpl implements HXTenderService {

    protected static final int DECIMAL_SCALE = 9;

    @Resource
    private BidManageDao bidManageDao;
    @Resource
    private BidManageService bidManageService;
    @Resource
    private CouponManageService couponManageService;
    @Resource
    private CouponManageDao couponManageDao;
    @Resource
    private HXCommonService hxCommonService;

    @Transactional
    @Override
    public String tender(int orderId, int userId, int clientType, String uri, Integer jxqId, String... userRedpacketIds) throws Exception {
        HXTradeType tradeType = HXTradeType.TB;//华兴交易类型
        String tradeCode = HXTradeType.getTradeCode(tradeType, clientType);
        HXAccountInfo userInfo = hxUserService.getAccountInfo(userId);
        T6504 tenderOrder = bidManageService.getTenderOrder(orderId);
        if (tenderOrder == null) {
            logger.warn("投标订单不存在，userId=[{}],orderId=[{}]", userId, orderId);
            throw new TradeException(TradeResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        T6230 bidInfo = bidManageService.process(tenderOrder.F03, tenderOrder.F04);//校验、暂扣减可投金额
        //创建华兴订单
        HXOrder order = new HXOrder().fromUser(userId, tradeType.getBusiCode(), orderId);
        huaXingService.createOrder(clientType, order);
        String flowNum = HXUtil.getChannelFlow(tradeCode, order.getId());
        super.submit(order.getId(), flowNum, null);//没有相应的业务订单，所以直接提交
        orderManageService.submit(orderId);//提交系统订单

        if (userRedpacketIds != null && userRedpacketIds.length > 0) {
            couponManageService.updateRedpacket(UserCouponState.LOCK, tenderOrder.F01, userRedpacketIds);//将红包锁定  3=锁定状态
        }
        if (jxqId != null && jxqId > 0) {
            couponManageDao.updateUserCoupon(jxqId, UserCouponState.LOCK, tenderOrder.F01);
        }

        HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
        //封装请求报文
        ReqBusinessParams params = new ReqBusinessParams();
        params.setTTRANS(String.valueOf(tradeType.getCode()));
        params.setACNO(StringHelper.decode(userInfo.getAcNo()));
        params.setACNAME(userInfo.getAcName());
        params.setAMOUNT(tenderOrder.F04.toString());
        params.setLOANNO(bidInfo.F25);
        params.setREMARK("");
        params.setRETURNURL(config.serverDomain() + uri);
        return MessageUtil.getMessageByBusi(params, tradeCode, flowNum, clientType);
    }

    @Override
    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
        //判断可投金额是否为 0，或者符合封标条件，如果符合，那么再去判断该标所有用户投资是否已成功，再进行封标
        //插入流水（往来账户->锁定/往来账户）
        String returnSate = busiParams.getRETURN_STATUS();
        int busiOrderId = order.getBusinessId();
        int userId = order.getUserId();
        T6501 t6501 = orderManageService.getOrder(busiOrderId);//获取系统业务订单
        if (t6501 != null && T6501_F03.DQR == t6501.F03) {
            T6504 tenderOrder = bidManageService.getTenderOrder(busiOrderId);
            if (tenderOrder == null) {
                logger.error("投标回调处理，找不到投标订单(t6504)，busiOrderId=[{}],hxOrderId=[{}]", busiOrderId, order.getId());
                throw new TradeException(TradeResponseCode.COMMON_RECORD_NOT_EXIST);
            }
            T6230 bidInfo = bidManageDao.getBidById(tenderOrder.F03);
            if (bidInfo == null) {
                throw new TradeException(TradeResponseCode.COMMON_RECORD_NOT_EXIST);
            }
            /*
            接口结果查询处理
             */
            if (HXConst.RETURN_STATUS_F.equals(returnSate)) {
                orderManageService.complete(busiOrderId, T6501_F03.SB);
                rollback(bidInfo, tenderOrder);
                order.setStatus(OrderStatus.SB.getCode());
                return;
            } else if (HXConst.ERRORCODE_FLOWNO_NOT_EXIST.equals(returnSate)) {
                orderManageService.complete(busiOrderId, T6501_F03.MJL);
                rollback(bidInfo, tenderOrder);
                order.setStatus(OrderStatus.MJL.getCode());
                return;
            } else if (HXConst.ORDER_STATE_TIMEOUT.equals(returnSate)) {
                orderManageService.complete(busiOrderId, T6501_F03.SB);
                rollback(bidInfo, tenderOrder);
                order.setStatus(OrderStatus.CS.getCode());
                return;
            }
            addFlows(bidInfo, tenderOrder);
            // 插入投标记录
            T6250 tenderRecord = new T6250();
            tenderRecord.F02 = bidInfo.F01;
            tenderRecord.F03 = tenderOrder.F02;
            tenderRecord.F04 = tenderOrder.F04;
            tenderRecord.F01 = tenderOrder.F05;
            // 判断计息金额与标总金额是否一致
            if (bidInfo.F05.compareTo(bidInfo.F26) == 0) {
                tenderRecord.F05 = tenderOrder.F04;
            } else {
                tenderRecord.F05 = bidInfo.F26.multiply(tenderOrder.F04).divide(bidInfo.F05, DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
            }
            tenderRecord.F07 = T6250_F07.F;
//            tenderRecord.F09 = //如果以后要记录是否是自动投标，可以在投标订单(T6504)里记录
            bidManageDao.addTenderRecord(tenderRecord);
            bidManageDao.updateTenderOrder(tenderOrder.F01, tenderRecord.F01);

            //可投金额满足封标条件的时候需要进行判断该标的投资订单是否还有待确认的
            if (TradeConsts.BID_FBJE.compareTo(bidInfo.F07) >= 0) {
                int count = bidManageDao.countTenderOrderOfDQR(bidInfo.F01, tenderOrder.F01);
                if (0 == count) {//封标
                    Map<String, Object> params = new HashMap<>(2);
                    params.put("F01", bidInfo.F01);
                    params.put("F11",  new Date());
                    bidManageDao.updateBidExInfo(params);
                    params.remove("F11");
                    params.put("F20",  T6230_F20.DFK.name());
                    bidManageDao.updateBid(params);
                } else {
                    logger.info("暂不能封标[{}]，还有[{}]笔投标订单没确认", bidInfo.F01, count);
                }
            }
            //发送短信
            String content = String.format("尊敬的用户：您已成功投资%s，投资金额为%s元。该项目满标即开始计息。", bidInfo.F03, tenderOrder.F04.toString());
            String letterSuffix = "如果您需要更多帮助，请查看帮助中心。您也可以随时拨打分利宝的客服热线400-930-5559，或发送邮件至kf@fenlibao.com，我们的客服人员会尽快帮您解答。";
            UserInfoEntity userInfo = userService.get(userId, null);
            tradeCommonService.sendLetter(userId, "投资通知", content + letterSuffix);
            tradeCommonService.sendMsg(userInfo.getPhone(), content, 0);
            //成功或失败，最后更改t6501的状态
            orderManageService.complete(busiOrderId, T6501_F03.CG);
            //发放优惠
            grantRedpacket(userId, tenderOrder.F01);
            grantCoupon(userId, tenderOrder.F01);
        }
    }

    /**
     * 添加流水
     * @param bidInfo
     * @param tenderOrder
     * @throws Exception
     */
    private void addFlows(T6230 bidInfo, T6504 tenderOrder) throws Exception {
    	AssetAccount rzzh;//入账账户
        int feeCode;
        if (bidInfo.F15 == T6230_F15.S) {//是否自动放款
            rzzh = userService.getFundAccount(bidInfo.F02, T6101_F03.HXWLZH);
            feeCode = TradeFeeCode.JK;
        } else {
            rzzh = userService.getFundAccount(tenderOrder.F02, T6101_F03.HXSDZH);
            feeCode = Integer.valueOf(bidInfo.F04 +""+TradeFeeCode.TZ);
        }
        if (rzzh == null) {
            throw new UserException(UserResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
        }
        AssetAccount czzh = userService.getFundAccount(tenderOrder.F02, T6101_F03.HXWLZH);//出账账户-投资者往来账户
        if (czzh == null) {
            throw new TradeException(TradeResponseCode.FUND_ACCOUNT_NOT_EXIST);
        }
        List<CapitalFlow> flows = new ArrayList<>(1);
        CapitalFlow whzhFlow = new CapitalFlow();
        whzhFlow.F02 = czzh.F01;
        whzhFlow.F03 = Integer.valueOf(bidInfo.F04 +""+TradeFeeCode.TZ);
        whzhFlow.F04 = rzzh.F01;
        whzhFlow.F07 = tenderOrder.F04;//支出
        whzhFlow.F08 = czzh.F06; //余额不维护
        whzhFlow.F09 = String.format("散标投资:%s，标题：%s", bidInfo.F25, bidInfo.F03);
        flows.add(whzhFlow);
        CapitalFlow sdzhFlow = new CapitalFlow();
        sdzhFlow.F02 = rzzh.F01;
        sdzhFlow.F03 = feeCode;
        sdzhFlow.F04 = czzh.F01;
        sdzhFlow.F06 = tenderOrder.F04;//收入
        sdzhFlow.F08 = rzzh.F06; //余额不维护
        sdzhFlow.F09 = String.format("散标投资:%s，标题：%s", bidInfo.F25, bidInfo.F03);
        flows.add(sdzhFlow);
        tradeCommonService.addTransactionFlows(flows);
    }

    /**
     * 发放返现券
     * @param userId
     * @param tenderId
     */
    private void grantRedpacket(int userId, int tenderId) {
        try {
            List<UserRedpacketVO>  userRedpacketList = couponManageDao.getUserRedpacket(userId, UserCouponState.LOCK, null, tenderId, null);//
            if (userRedpacketList != null && userRedpacketList.size() > 0) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                String[] redpacketIds = new String[]{};
                int i = 0;
                for (UserRedpacketVO vo : userRedpacketList) {
                    totalAmount = totalAmount.add(vo.getAmount());
                    redpacketIds[i] = vo.getUserRedpacketId().toString();
                    i++;
                }
                //单笔发放
                boolean isSuccess = hxCommonService.singleRewards(userId, totalAmount, "投标["+tenderId+"]返现");
                if (isSuccess) {
                    couponManageService.updateRedpacket(UserCouponState.USED, tenderId, redpacketIds);
                } else {
                    logger.error("投标回发放红包失败，userId=[{}],tenderId=[{]]", userId, tenderId);
                }
            }
        } catch (Exception e) {
            logger.error("投标回发放红包失败，userId=[{}],tenderId=[{}]", userId, tenderId);
        }
    }

    /**
     * 发放加息券
     * @param userId
     * @param tenderId
     */
    private void grantCoupon(int userId, int tenderId) {
        try {
            List<UserCouponVO> userCouponList = couponManageDao.getUserCoupon(userId,UserCouponState.LOCK,tenderId,null,null);
            if (userCouponList != null && userCouponList.size() > 0) {
                for (UserCouponVO vo : userCouponList) {
                    couponManageDao.updateUserCoupon(vo.getUserCouponId(), UserCouponState.USED, tenderId);
                }
            }
        } catch (Exception e) {
            logger.error("投标回调使用加息券失败,userId=[{}],tenderId=[{}]", userId, tenderId);
        }
    }

    private void rollback(T6230 bidInfo, T6504 tenderOrder) throws Exception {
        int userId = tenderOrder.F02;
        int tenderId = tenderOrder.F01;
        /*
        回滚可投金额
         */
        Map<String, Object> bidParams = new HashMap<>(2);
        bidParams.put("F01", bidInfo.F01);
        bidParams.put("F07", bidInfo.F07.add(tenderOrder.F04));
        bidManageDao.updateBid(bidParams);//将可投金额加回来
        /*
         回滚返现券
         */
        List<UserRedpacketVO>  userRedpacketList = couponManageDao.getUserRedpacket(userId, UserCouponState.LOCK, null, tenderId, null);//
        if (userRedpacketList != null && userRedpacketList.size() > 0) {
            String[] redpacketIds = new String[]{};
            int i = 0;
            for (UserRedpacketVO vo : userRedpacketList) {
                redpacketIds[i] = vo.getUserRedpacketId().toString();
                i++;
            }
            couponManageService.updateRedpacket(UserCouponState.UNUSED, tenderOrder.F01, redpacketIds);
        }
        /*
        回滚加息券
         */
        List<UserCouponVO> userCouponList = couponManageDao.getUserCoupon(userId,UserCouponState.LOCK,tenderId,null,null);
        if (userCouponList != null && userCouponList.size() > 0) {
            for (UserCouponVO vo : userCouponList) {
                couponManageDao.updateUserCoupon(vo.getUserCouponId(), UserCouponState.UNUSED, tenderId);
            }
        }
    }

}
