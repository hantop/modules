package com.fenlibao.p2p.model.entity.coupons;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券
 */
public class Coupon {
    private int id;
    private String couponCode;// 优惠券编码
    private int couponTypeId;// 优惠券类型ID
    private Integer effectDay;// 优惠券有效天数
    private Integer maxInvestMoney;// 投资金额上限
    private Integer minInvestMoney;// 投资金额下限
    private Integer maxInvestDay;// 投资期限上限
    private Integer minInvestDay;// 投资期限下限
    private BigDecimal scope;// 优惠幅度
    private Date createTime;
    private Date updateTime;
    private Integer grantStatus;//发放状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(int couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public Integer getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(Integer maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public Integer getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(Integer minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Integer grantStatus) {
        this.grantStatus = grantStatus;
    }
}