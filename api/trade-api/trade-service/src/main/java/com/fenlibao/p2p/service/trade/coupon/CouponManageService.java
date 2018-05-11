package com.fenlibao.p2p.service.trade.coupon;

import com.fenlibao.p2p.model.trade.enums.coupon.UserCouponState;

import java.math.BigDecimal;

/**
 * Created by zcai on 2016/12/19.
 */
public interface CouponManageService {

    /**
     * 校验红包是否有效
     * @param userId
     * @param bidId
     * @param amount
     * @param userJxqId 用户加息券ID
     * @param userRedpacketIds 返现券ID
     * @throws Exception
     */
    void validateUserCoupons(int userId, int bidId, BigDecimal amount, Integer userJxqId, String... userRedpacketIds) throws Exception;

    /**
     * 更新红包
     * @param ids
     * @param state
     * @param tenderId
     * @throws Exception
     */
    void updateRedpacket(UserCouponState state, Integer tenderId, String... ids) throws Exception;

}
