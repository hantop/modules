package com.fenlibao.p2p.model.entity;

/**
 * Created by Administrator on 2017/2/6.
 */
public class PlanBidsStatus {

    private Integer planId;//计划id

    private Integer bidId;//标id

    private String status;//标状态

    private String surplusAmount;//可投金额

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
}
