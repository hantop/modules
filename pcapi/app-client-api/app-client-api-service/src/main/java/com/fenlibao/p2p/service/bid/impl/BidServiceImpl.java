package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.dao.ShopTreasureDao;
import com.fenlibao.p2p.dao.coupon.CouponDao;
import com.fenlibao.p2p.model.entity.ShopTreasureInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.BidService;
import com.fenlibao.p2p.service.bid.IBidDmService;
import com.fenlibao.p2p.service.bid.migrate.impl.TenderOrderExecutor;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.trade.ITradeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LouisWang on 2015/8/14.
 */
@Service
public class BidServiceImpl implements BidService {
    private static final Logger logger = LogManager.getLogger(BidServiceImpl.class);
    @Resource
    private IBidDmService bidDmService;
    @Resource
    private TenderOrderExecutor tenderOrderExecutor;
    @Resource
    private ITradeService iTradeService;
    @Resource
    private ShopTreasureDao shopTreasureDao;
    @Resource
    private RedpacketService redpacketService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private CouponDao couponDao;

    @Override
    public void doBid(int bidId, BigDecimal amount, int accountId, String experFlg, String fxhbIds) throws Throwable {

        Map<String, String> rtnMap = bidDmService.doBid(bidId, amount, accountId, experFlg, fxhbIds);
        //余额投标订单
        tenderOrderExecutor.submit(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
        //确认订单
        tenderOrderExecutor.confirm(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);

        //是否包含返现红包
        if (!StringUtils.isBlank(fxhbIds)) {
            List<UserRedPacketInfo> userRedPacketInfos = null;
            InverstBidTradeInfo inverstBidTradeInfo = null;
            UserInfo userInfo = null;
            try {
                // 订单详情
                inverstBidTradeInfo = redpacketService.getBidOrderDetail(IntegerParser.parse(rtnMap.get("orderId")));
                if (inverstBidTradeInfo == null) {
                    throw new LogicalException("30610"); //订单明细记录不存在
                }
                // 发站内信 获取用户信息
                userInfo = userInfoService.getUser(null, null, String.valueOf(inverstBidTradeInfo.getUserId()));
                if (userInfo == null) {
                    throw new LogicalException("30631");     //用户记录不存在
                }

                Map<String, Object> params = new HashMap<String, Object>();
                String[] hbIdArr = fxhbIds.split("\\|");
                params.put("fxhbIdArr", hbIdArr);

                userRedPacketInfos = redpacketService.getBidRedpacket(params);
                //消费返现红包
                redpacketService.grantRedPackets(IntegerParser.parse(rtnMap.get("orderId")), userRedPacketInfos, String.valueOf(userInfo.getUserId()), userInfo.getPhone(), FeeCode.REGISTERRETURNCACH_REDPACKET, VersionTypeEnum.PT);
                //更新标和红包的状态
                for (UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                    Map<String, Object> tmpParam = new HashMap<String, Object>();
                    tmpParam.put("bidId", inverstBidTradeInfo.getBidId());
                    tmpParam.put("status", InterfaceConst.FXHB_TRADE_STATUS);
                    tmpParam.put("fxhbId", userRedPacketInfo.getId());

                    redpacketService.updateRedpacketsRelation(tmpParam);
                }
            } catch (Exception e) {
                String message = "[申购时返现红包使用失败：]";
                logger.error(message, e);
                // 申购时返现红包发放失败
                HashMap<String, Object> errMap = new HashMap<>();
                errMap.put("userId", accountId);
                errMap.put("userRedpacketId", fxhbIds);
                if (inverstBidTradeInfo != null) {
                    errMap.put("bidId", inverstBidTradeInfo.getBidId());
                }
                errMap.put("message", message);
                errMap.put("redType", FeeCode.REGISTERRETURNCACH_REDPACKET);
                try {
                    redpacketService.recordRedpackExceptionLog(errMap);
                } catch (Exception e1) {
                    logger.error("[注册现金红包插入参数:]" + errMap.toString(), e1);
                }
            }
        }
    }

    @Override
    public void doBid(int bidId, BigDecimal amount, int accountId, String experFlg, String fxhbIds, String jxqId) throws Throwable {

        //投标之前，验证加息券是否符合使用条件
        if (StringUtils.isNotEmpty(jxqId)) {
            Map<String, Object> map = new HashMap<>();
            map.put("bid", bidId);
            map.put("userId", accountId);
            map.put("couponId", jxqId);
            map.put("money", amount);
            int res = couponDao.checkCouponBeforeInvest(map);
            if (res != 1) {
                //throw new BusinessException(ResponseCode.BID_COUPON_CONDITIONS_NOT_SATISFIED);
                throw new LogicalException("120319");
            }
        }

        Map<String, String> rtnMap = bidDmService.doBid(bidId, amount, accountId, experFlg, fxhbIds);
        //余额投标订单
        tenderOrderExecutor.submit(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
        //确认订单
        tenderOrderExecutor.confirm(IntegerParser.parse(rtnMap.get("orderId")), rtnMap);

        //是否包含返现红包
        if (!StringUtils.isBlank(fxhbIds)) {
            //消费红包，更新红包状态
            grantAndUpdateRedPackets(fxhbIds,rtnMap,accountId);
        } else if (!StringUtils.isBlank(jxqId)) {
            //使用加息卷
            InverstBidTradeInfo inverstBidTradeInfo = null;
            try {
                // 订单详情
                inverstBidTradeInfo = redpacketService.getBidOrderDetail(IntegerParser.parse(rtnMap.get("orderId")));
                if (inverstBidTradeInfo == null) {
                    logger.error("投标记录不存在，orderId=[{}]", rtnMap.get("orderId"));
                    throw new LogicalException("30610"); //订单明细记录不存在
                }
                int recordId = inverstBidTradeInfo.getBidRecordId();
                Map<String, Object> paramMap = new HashMap<>(1);
                paramMap.put("recordId", recordId);
                paramMap.put("userCouponId", jxqId);
                paramMap.put("userId", inverstBidTradeInfo.getUserId());
                int result = couponDao.updateUserCoupon(paramMap);
                if (result != 1) {
                    logger.error(String.format("[申购时加息卷更新信息失败：],加息卷id:%s,投标订单id:%s"
                            , jxqId, rtnMap.get("orderId")));
                }
            } catch (Exception e) {
                logger.error(String.format("[申购时加息卷使用异常：],加息卷id:%s,投标订单id:%s"
                        , jxqId, rtnMap.get("orderId")), e);
            }
        }
    }

    @Override
    public boolean isValidPassword(int accountId, String tradePassword) throws Throwable {

        //先校验是否需开启免交易密码
        /*目前所有加入都需要密码验证  免密功能先屏蔽掉
    	try {
    		Map<String, Object> validData = bankService.getUserDealStatus(accountId);
    		if(validData.isEmpty()){
            	throw new LogicalException("30616");   //查询记录有误,没有查询到用户信息
            }
		} catch (Exception e1) {
			throw new LogicalException("30616");   //查询记录有误,没有查询到用户信息
		}
		*/
        String tradePwd = "";
        try {
            tradePwd = PasswordCryptUtil.cryptAESPassword(tradePassword);
            if (StringUtils.isBlank(tradePwd)) {
                throw new LogicalException("30621"); //交易密码验证出现异常
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LogicalException("30621"); //交易密码验证出现异常
        }
        //获取用户交易密码
        String isPassword = iTradeService.getTradePassword(accountId);
        if (StringUtils.isBlank(isPassword)) {
            throw new LogicalException("30617"); //没有设置交易密码
        }
        if (!tradePwd.equals(isPassword)) {
            throw new LogicalException("30615"); //交易密码错误
        }
        return true;
    }

    @Override
    public ShopTreasureInfo findShopTreasureInfo(int bid) throws Throwable {
        //获取开店宝计划详情
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", bid);
        map.put("proType", InterfaceConst.PRO_TYPE_KDB);//产品类型
        ShopTreasureInfo info = shopTreasureDao.getShopTreasureInfo(map);
        return info;
    }


    /**
     * 消费红包，更新红包状态
     * */
    private void grantAndUpdateRedPackets(String fxhbIds,Map<String, String> rtnMap
            ,int accountId) {
        List<UserRedPacketInfo> userRedPacketInfos = null;
        InverstBidTradeInfo inverstBidTradeInfo = null;
        UserInfo userInfo = null;
        try {
            // 订单详情
            inverstBidTradeInfo = redpacketService.getBidOrderDetail(IntegerParser.parse(rtnMap.get("orderId")));
            if (inverstBidTradeInfo == null) {
                throw new LogicalException("30610"); //订单明细记录不存在
            }
            // 发站内信 获取用户信息
            userInfo = userInfoService.getUser(null, null, String.valueOf(inverstBidTradeInfo.getUserId()));
            if (userInfo == null) {
                throw new LogicalException("30631");     //用户记录不存在
            }

            Map<String, Object> params = new HashMap<String, Object>();
            String[] hbIdArr = fxhbIds.split("\\|");
            params.put("fxhbIdArr", hbIdArr);

            userRedPacketInfos = redpacketService.getBidRedpacket(params);
            //消费返现红包
            redpacketService.grantRedPackets(IntegerParser.parse(rtnMap.get("orderId")), userRedPacketInfos, String.valueOf(userInfo.getUserId()), userInfo.getPhone(), FeeCode.REGISTERRETURNCACH_REDPACKET, VersionTypeEnum.PT);
            //更新标和红包的状态
            for (UserRedPacketInfo userRedPacketInfo : userRedPacketInfos) {
                Map<String, Object> tmpParam = new HashMap<String, Object>();
                tmpParam.put("bidId", inverstBidTradeInfo.getBidId());
                tmpParam.put("status", InterfaceConst.FXHB_TRADE_STATUS);
                tmpParam.put("fxhbId", userRedPacketInfo.getId());

                redpacketService.updateRedpacketsRelation(tmpParam);
            }
        } catch (Exception e) {
            String message = "[申购时返现红包使用失败：]";
            logger.error(message, e);
            // 申购时返现红包发放失败
            HashMap<String, Object> errMap = new HashMap<>();
            errMap.put("userId", accountId);
            errMap.put("userRedpacketId", fxhbIds);
            if (inverstBidTradeInfo != null) {
                errMap.put("bidId", inverstBidTradeInfo.getBidId());
            }
            errMap.put("message", message);
            errMap.put("redType", FeeCode.REGISTERRETURNCACH_REDPACKET);
            try {
                redpacketService.recordRedpackExceptionLog(errMap);
            } catch (Exception e1) {
                logger.error("[注册现金红包插入参数:]" + errMap.toString(), e1);
            }
        }
    }

}

//  //先校验是否需开启免交易密码
//    Map<String,Object> validData = bankService.getUserDealStatus(accountId);
//    System.out.println(validData);
//    System.out.println(tradePassword);
//    if(!validData.isEmpty()){
//        //需要交易密码 按钮先不使用
//       // boolean tradeKey = (boolean) validData.get("tradersPwdStatus");
//        //   if(tradeKey){
//
//            String tradePwd = PasswordCryptUtil.cryptAESPassword(tradePassword);
//            String isPassword = iTradeService.getTradePassword(accountId);
//            if(!StringUtils.isBlank(isPassword)){
//                if(!tradePwd.equals(isPassword)){
//                    throw new LogicalException("30615"); //交易密码错误
//                }else {
//                    validBid = true;
//                }
//            }else {
//                throw new LogicalException("30617"); //没有设置交易密码
//            }
//
//        /*}else {
//            throw new LogicalException("30617");   //没有开启交易密码
//        }*/
//    }else {
//        throw new LogicalException("30616");   //查询记录有误,没有查询到用户信息
//    }