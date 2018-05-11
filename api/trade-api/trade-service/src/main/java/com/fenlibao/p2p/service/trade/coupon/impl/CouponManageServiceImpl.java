package com.fenlibao.p2p.service.trade.coupon.impl;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.coupon.CouponManageDao;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.coupon.UserCouponState;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.trade.vo.UserCouponVO;
import com.fenlibao.p2p.model.trade.vo.UserRedpacketVO;
import com.fenlibao.p2p.service.trade.coupon.CouponManageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zcai on 2016/12/19.
 */
@Service
public class CouponManageServiceImpl implements CouponManageService {

    private static final Logger logger = LogManager.getLogger(CouponManageServiceImpl.class);

    @Resource
    private CouponManageDao couponManageDao;
    @Resource
    private BidManageDao bidManageDao;

    @Override
    public void validateUserCoupons(int userId, int bidId, BigDecimal amount, Integer userJxqId, String... userRedpacketIds) throws Exception {
        if (userJxqId == null && userRedpacketIds == null) {
            return;
        }
        T6230 bidInfo = bidManageDao.getBidById(bidId);
        if (null != userRedpacketIds && userRedpacketIds.length > 0) {
            List<UserRedpacketVO>  userRedpacketList = couponManageDao.getUserRedpacket(userId, UserCouponState.UNUSED, bidInfo.F04, null, userRedpacketIds);//状态为 1 是未使用
            if (null == userRedpacketList || userRedpacketIds.length != userRedpacketList.size()) {
                logger.error("投标返现券校验不通过...userId=[{}],bidId=[{}],userRedpacketIds=[{}]", userId, bidId, userRedpacketIds.toString());
                throw new TradeException(TradeResponseCode.COUPON_REDPACKET_INCORRECT);
            }
            Date now = new Date();
            int investDeadline = bidInfo.F09 == 0 ? bidInfo.F32 : 30 * bidInfo.F09;
            BigDecimal investAmount = BigDecimal.ZERO;
            for (UserRedpacketVO redpacketVO : userRedpacketList) {
                if (redpacketVO.getInvestDeadline() != null && investDeadline < redpacketVO.getInvestDeadline()) {
                    throw new TradeException(TradeResponseCode.COUPON_REDPACKET_INVESTDEADLINE_INCORRECT);
                }
                investAmount = investAmount.add(redpacketVO.getInvestAmount());
                if (now.after(redpacketVO.getValidDate())) {
                    throw new TradeException(TradeResponseCode.COUPON_REDPACKET_OVERDUE);
                }
            }
            if (investAmount.compareTo(amount) > 0) {
                throw new TradeException(TradeResponseCode.COUPON_REDPACKET_INVESTAMOUNT_INCORRECT);
            }
        }
        if (userJxqId != null && userJxqId > 0) {
            validateJXQ(userId, bidInfo, userJxqId, amount);
        }
    }

    @Transactional
    @Override
    public void updateRedpacket(UserCouponState state, Integer tenderId, String... ids) throws Exception {
        for (int i = 0; i < ids.length; i++) {
            couponManageDao.updateRedpacket(Integer.parseInt(ids[i]), state, tenderId);
        }
    }


    private void validateJXQ(int userId, T6230 bidInfo, Integer userJxqId, BigDecimal amount) throws Exception {
        List<UserCouponVO> userCouponList = couponManageDao.getUserCoupon(userId, UserCouponState.UNUSED, null, bidInfo.F04, userJxqId);
        if (userCouponList == null || userCouponList.size() == 0) {
            logger.error("投标加息券校验不通过,userId=[{}],bidId=[{}],jxqId=[{}],amount=[{}]", userId, bidInfo.F01, userJxqId, amount);
            throw new TradeException(TradeResponseCode.COUPON_INFO_INCORRECT);
        }
        Date now = new Date();
        int investDeadline = bidInfo.F09 == 0 ? bidInfo.F32 : 30 * bidInfo.F09;
        for (UserCouponVO coupon : userCouponList) {
            if (now.after(coupon.getValidTime())) {
                throw new TradeException(TradeResponseCode.COUPON_CONDITIONS_OF_USE_INCORRECT);
            }
            if (amount.compareTo(coupon.getMaxInvestAmount()) > 0 || amount.compareTo(coupon.getMinInvestAmount()) < 0) {
                throw new TradeException(TradeResponseCode.COUPON_CONDITIONS_OF_USE_INCORRECT);
            }
            if ((coupon.getMaxInvestDay() != null && investDeadline > coupon.getMaxInvestDay())
                    || (coupon.getMinInvestDay() != null && investDeadline < coupon.getMinInvestDay())) {
                throw new TradeException(TradeResponseCode.COUPON_CONDITIONS_OF_USE_INCORRECT);
            }
        }
    }

}
