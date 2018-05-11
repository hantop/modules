package com.fenlibao.model.pms.da.biz;

import java.math.BigDecimal;

/**
 * 标的
 * Created by chenzhixuan on 2017/2/7.
 */
public class Loan {
    private int id;
    private int planId;
    private int bidId;
    private String planName;
    private String loanName;
    private BigDecimal amount;// 计划借款总金额
    private int cycle;// 借款期限
    private String cycleType;// 借款周期类型（按天/按月）
    private BigDecimal rate;// 发标利率
    private String rateStr;// 发标利率
    private BigDecimal raisedRate;// 加息利率
    private String raisedRateStr;// 加息利率
    private String status;// 状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }

    public BigDecimal getRaisedRate() {
        return raisedRate;
    }

    public void setRaisedRate(BigDecimal raisedRate) {
        this.raisedRate = raisedRate;
    }

    public String getRaisedRateStr() {
        return raisedRateStr;
    }

    public void setRaisedRateStr(String raisedRateStr) {
        this.raisedRateStr = raisedRateStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}