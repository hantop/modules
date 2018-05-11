package com.fenlibao.p2p.model.entity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/2/6.
 */
public class PlanBidsStatus {

    private Integer planId;//计划id

    private Integer bidId;//标id

    private String status;//标状态

    private String surplusAmount;//可投金额

    private BigDecimal yearYield;

    double interestRise;//加息券加息利率

    double bidInterestRise;//加息标加息利率

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public BigDecimal getYearYield() {
        return yearYield;
    }

    public void setYearYield(BigDecimal yearYield) {
        this.yearYield = yearYield;
    }

    public double getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(double bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public double getInterestRise() {
        return interestRise;
    }

    public void setInterestRise(double interestRise) {
        this.interestRise = interestRise;
    }
}
