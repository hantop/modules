package com.fenlibao.p2p.model.entity.coupons;

/**
 * Created by xiao on 2016/10/18.
 * <p>
 * 优惠券（加息、返现）统计
 */
public class UserCouponStatisticsInfo {
    private int unusedCount; //未使用的数量
    private int usedCount;   //已使用的数量
    private int expiredCount;//已过期的数量

    public int getExpiredCount() {
        return expiredCount;
    }

    public void setExpiredCount(int expiredCount) {
        this.expiredCount = expiredCount;
    }

    public int getUnusedCount() {
        return unusedCount;
    }

    public void setUnusedCount(int unusedCount) {
        this.unusedCount = unusedCount;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }
}
