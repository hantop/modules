package com.fenlibao.p2p.model.entity.coupons;

import java.util.ArrayList;
import java.util.List;

/**
 * 加息券
 */
public class RateCoupon extends Coupon {
    private List<Integer> bidTypeIds = new ArrayList<>(); //对象映射,来自对应的标的限制,一对多(不存入这个实体类中对应的数据库表)
    private int activityId;// 活动ID

    public List<Integer> getBidTypeIds() {
        return bidTypeIds;
    }

    public void setBidTypeIds(List<Integer> bidTypeIds) {
        this.bidTypeIds = bidTypeIds;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }
}