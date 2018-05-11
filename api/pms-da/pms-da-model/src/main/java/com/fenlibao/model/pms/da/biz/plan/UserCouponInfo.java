package com.fenlibao.model.pms.da.biz.plan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户的优惠劵
 */

public class UserCouponInfo implements Serializable {

    private Integer id; //自增id
    private Integer couponId;   //优惠券ID
    private Integer userId; //用户Id
    private Integer activityId;//活动ID
    private Integer investId;//投资记录ID
    private Integer effectDay;// 优惠券有效天数
    private Date validTime; //优惠券活动截止使用日期
    private Integer surplusDays;// 优惠券剩余有效天数
    private BigDecimal scope = BigDecimal.ZERO;   //优惠幅度:加息利率
    private BigDecimal maxInvestMoney = BigDecimal.ZERO;  //投资金额上限
    private BigDecimal minInvestMoney = BigDecimal.ZERO;  //投资金额下限
    private Integer maxInvestDay; //投资期限上限(小于XX天)
    private Integer minInvestDay; //投资期限下限(大于XX天)
    private String status;  //优惠券状态
    private List<BidTypeVO> BidTypes; //标类型
    private Integer overdueFlag; //过期提示
    private Integer times;//可以分享的次数
    private Integer userPlanId;
    private Integer InvestType;

    public Integer getUserPlanId() {
		return userPlanId;
	}

	public void setUserPlanId(Integer userPlanId) {
		this.userPlanId = userPlanId;
	}

	public Integer getInvestType() {
		return InvestType;
	}

	public void setInvestType(Integer investType) {
		InvestType = investType;
	}

	public Integer getOverdueFlag() {
        return overdueFlag;
    }

    public void setOverdueFlag(Integer overdueFlag) {
        this.overdueFlag = overdueFlag;
    }

    public Integer getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(Integer surplusDays) {
        this.surplusDays = surplusDays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getInvestId() {
        return investId;
    }

    public void setInvestId(Integer investId) {
        this.investId = investId;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public BigDecimal getScope() {
        return scope;
    }

    public void setScope(BigDecimal scope) {
        this.scope = scope;
    }

    public BigDecimal getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(BigDecimal maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public BigDecimal getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(BigDecimal minInvestMoney) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BidTypeVO> getBidTypes() {
        return BidTypes;
    }

    public void setBidTypes(List<BidTypeVO> bidTypes) {
        BidTypes = bidTypes;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
