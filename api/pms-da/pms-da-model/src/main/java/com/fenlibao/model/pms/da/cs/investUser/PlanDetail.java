package com.fenlibao.model.pms.da.cs.investUser;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 计划详情
 * Created by Administrator on 2017/12/18.
 */
public class PlanDetail {

    private String bidType;
    private String bidName;
    private BigDecimal matchAmount;
    private BigDecimal rate;
    private int loanDays;
    private int loanMonths;
    private String paybackWay;
    private Date matchTime;
    private Date exitTime;
    private String bidStatus;

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public BigDecimal getMatchAmount() {
        return matchAmount;
    }

    public void setMatchAmount(BigDecimal matchAmount) {
        this.matchAmount = matchAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public int getLoanMonths() {
        return loanMonths;
    }

    public void setLoanMonths(int loanMonths) {
        this.loanMonths = loanMonths;
    }

    public String getPaybackWay() {
        return paybackWay;
    }

    public void setPaybackWay(String paybackWay) {
        this.paybackWay = paybackWay;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }
}
