package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 用户加息券
 *
 * @author chenzhixuan
 */
public class UserRateCoupon implements Comparable<UserRateCoupon> {
    private int id;

    private Integer userId;

    private Integer rateCouponId;

    private String couponCode;

    private Byte couponStatus;

    private BigDecimal scope;// 加息幅度

    private Integer bidId;

    private Date validTime;

    private Integer grantId;

    private Byte grantStatus;

    //额外字段
    private String grantStatusName;
    private String phone;
    private String activityCode;
    private int activityId;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer effectDay;

    private BigDecimal redMoney;

    private BigDecimal investMoney;

    private int reawrdType;// 奖励类型

    private String grantName;//导入报表的名字

    @Override
    public int compareTo(UserRateCoupon o) {
        boolean flag = this.getRateCouponId() == o.getRateCouponId() && this.getUserId() == o.getUserId();
        return flag ? 1 : -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRateCouponId() {
        return rateCouponId;
    }

    public void setRateCouponId(Integer rateCouponId) {
        this.rateCouponId = rateCouponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Byte getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Byte couponStatus) {
        this.couponStatus = couponStatus;
    }

    public BigDecimal getScope() {
        return scope;
    }

    public void setScope(BigDecimal scope) {
        this.scope = scope;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public Integer getGrantId() {
        return grantId;
    }

    public void setGrantId(Integer grantId) {
        this.grantId = grantId;
    }

    public Byte getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Byte grantStatus) {
        this.grantStatus = grantStatus;
    }

    public String getGrantStatusName() {
        return grantStatusName;
    }

    public void setGrantStatusName(String grantStatusName) {
        this.grantStatusName = grantStatusName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public int getReawrdType() {
        return reawrdType;
    }

    public void setReawrdType(int reawrdType) {
        this.reawrdType = reawrdType;
    }

    public String getGrantName() {
        return grantName;
    }

    public void setGrantName(String grantName) {
        this.grantName = grantName;
    }
}