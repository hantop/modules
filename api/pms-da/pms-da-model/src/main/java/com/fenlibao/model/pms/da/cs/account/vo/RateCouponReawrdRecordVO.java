package com.fenlibao.model.pms.da.cs.account.vo;

import com.fenlibao.model.pms.da.cs.account.ReawrdRecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 加息券奖励记录
 * <p>
 * Created by chenzhixuan on 2016/12/8.
 */
public class RateCouponReawrdRecordVO extends ReawrdRecord {
    private int couponId;
    private int grantId;
    private int activityId;
    private String couponCode;
    private Byte couponStatus;
    private String bidName;
    private String channelName;
    private Date activateTime;
    private Integer effectDay;// 优惠券有效天数
    private Integer maxInvestMoney;// 投资金额上限
    private Integer minInvestMoney;// 投资金额下限
    private Integer maxInvestDay;// 投资期限上限
    private Integer minInvestDay;// 投资期限下限
    private BigDecimal scope;// 优惠幅度
    private Date createTime;// 创建时间
    private Integer grantStatus;//发放状态
    private List<String> productTypes;// 产品类型
    private String bidTypeStr;

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getGrantId() {
        return grantId;
    }

    public void setGrantId(int grantId) {
        this.grantId = grantId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
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

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Date getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(Date activateTime) {
        this.activateTime = activateTime;
    }

    @Override
    public Integer getEffectDay() {
        return effectDay;
    }

    @Override
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

    public Integer getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Integer grantStatus) {
        this.grantStatus = grantStatus;
    }

    public List<String> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<String> productTypes) {
        this.productTypes = productTypes;
    }

    public String getBidTypeStr() {
        return bidTypeStr;
    }

    public void setBidTypeStr(String bidTypeStr) {
        this.bidTypeStr = bidTypeStr;
    }
}
