package com.fenlibao.p2p.model.trade.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券
 * Created by zcai on 2016/12/23.
 */
public class UserCouponVO {

    private Integer userCouponId;//用户优惠券ID
    private Integer couponId;//优惠券ID
    private Date validTime;//有效时间
    private Integer state;//状态(1:未使用 2:已使用 3:锁定)
    private BigDecimal maxInvestAmount;//最大投资金额
    private BigDecimal minInvestAmount;//最小投资金额
    private Integer maxInvestDay;//最大投资天数
    private Integer minInvestDay;//最小投资天数
    private BigDecimal scope;//加息幅度

    private List<BidType_> bidTypes; //标类型

    public List<BidType_> getBidTypes() {
        return bidTypes;
    }

    public void setBidTypes(List<BidType_> bidTypes) {
        bidTypes = bidTypes;
    }

    public Integer getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(Integer userCouponId) {
        this.userCouponId = userCouponId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BigDecimal getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(BigDecimal maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public BigDecimal getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(BigDecimal minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public Integer getMaxInvestDay() {
        return maxInvestDay;
    }

    public void setMaxInvestDay(Integer maxInvestDay) {
        this.maxInvestDay = maxInvestDay;
    }

    public Integer getMinInvestDay() {
        return minInvestDay;
    }

    public void setMinInvestDay(Integer minInvestDay) {
        this.minInvestDay = minInvestDay;
    }

    public BigDecimal getScope() {
        return scope;
    }

    public void setScope(BigDecimal scope) {
        this.scope = scope;
    }
}
