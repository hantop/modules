package com.fenlibao.p2p.model.xinwang.enums.coupon;

/**
 * flb.t_user_redpackets.status
 * ||
 * flb.t_user_coupon.coupon_status
 */
public enum CouponState {

    /**
     * 未使用
     */
    UNUSED(1),
    /**
     * 已使用
     */
    USED(2);

    private int code;

    CouponState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
