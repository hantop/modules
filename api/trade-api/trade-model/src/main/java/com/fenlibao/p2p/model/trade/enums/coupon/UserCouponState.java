package com.fenlibao.p2p.model.trade.enums.coupon;

/**
 * flb.t_user_redpackets.status
 * ||
 * flb.t_user_coupon.coupon_status
 */
public enum UserCouponState {

    /**
     * 未使用
     */
    UNUSED(1),
    /**
     * 已使用
     */
    USED(2),
    /**
     * 锁定
     */
    LOCK(3),
    ;

    private int code;

    UserCouponState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
