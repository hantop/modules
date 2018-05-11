package com.fenlibao.model.pms.da.cs.account.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class UserInvestPlan {

    private String planId;// 用户投资计划id

    private String recordId;// 用户投资计划记录id

    private String planType;// 区分新旧计划

    private int type;// 计划类型

    private int userId;

    private String name;// 名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date investTime;// 投资计划时间

    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date bearrateDate;// 计息时间

    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date expireTime;// 到期时间

    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date settleTime; // 结清时间
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date applyExitTime; // 申请退出时间/当前时间

    private BigDecimal investRate;// 投资利率

    private BigDecimal minYearlyRate;// 最低投资利率

    private BigDecimal maxYearlyRate;// 最高投资利率

    private BigDecimal tradeAmount;// 投资金额

    private String cycleType;// 周期类型

    private int cycle;// 周期

    private BigDecimal interestAmount; // 预期总收益/实际总收益

    private String origenRate;// 原收益

    private String planInterate;// 计划加息收益

    private String interest;// 加息券加息收益

    private String paymentInterate;// 逾期收益

    private String penalty;// 提前还款违约金

    private BigDecimal bidScope;// 标加息幅度/计划加息幅度

    private BigDecimal scope;// 加息券加息幅度

    private BigDecimal redPacketMoney;// 返现金额

    private String status;// 状态

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public Date getBearrateDate() {
        return bearrateDate;
    }

    public void setBearrateDate(Date bearrateDate) {
        this.bearrateDate = bearrateDate;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    public Date getApplyExitTime() {
        return applyExitTime;
    }

    public void setApplyExitTime(Date applyExitTime) {
        this.applyExitTime = applyExitTime;
    }

    public BigDecimal getInvestRate() {
        return investRate;
    }

    public void setInvestRate(BigDecimal investRate) {
        this.investRate = investRate;
    }

    public BigDecimal getMinYearlyRate() {
        return minYearlyRate;
    }

    public void setMinYearlyRate(BigDecimal minYearlyRate) {
        this.minYearlyRate = minYearlyRate;
    }

    public BigDecimal getMaxYearlyRate() {
        return maxYearlyRate;
    }

    public void setMaxYearlyRate(BigDecimal maxYearlyRate) {
        this.maxYearlyRate = maxYearlyRate;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getOrigenRate() {
        return origenRate;
    }

    public void setOrigenRate(String origenRate) {
        this.origenRate = origenRate;
    }

    public String getPlanInterate() {
        return planInterate;
    }

    public void setPlanInterate(String planInterate) {
        this.planInterate = planInterate;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getPaymentInterate() {
        return paymentInterate;
    }

    public void setPaymentInterate(String paymentInterate) {
        this.paymentInterate = paymentInterate;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getBidScope() {
        return bidScope;
    }

    public void setBidScope(BigDecimal bidScope) {
        this.bidScope = bidScope;
    }

    public BigDecimal getScope() {
        return scope;
    }

    public void setScope(BigDecimal scope) {
        this.scope = scope;
    }

    public BigDecimal getRedPacketMoney() {
        return redPacketMoney;
    }

    public void setRedPacketMoney(BigDecimal redPacketMoney) {
        this.redPacketMoney = redPacketMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
