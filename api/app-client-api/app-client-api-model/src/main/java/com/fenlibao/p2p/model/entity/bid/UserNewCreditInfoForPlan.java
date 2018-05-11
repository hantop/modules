package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;

/**
 * Created by xiao on 2017/3/22.
 */
public class UserNewCreditInfoForPlan {
    private int newCreditId;
    private BigDecimal amount;
    private String status;
    private int bidRecordId;

    public UserNewCreditInfoForPlan() {

    }

    public UserNewCreditInfoForPlan(int newCreditId, BigDecimal amount, String status, int bidRecordId) {

        this.newCreditId = newCreditId;
        this.amount = amount;
        this.status = status;
        this.bidRecordId = bidRecordId;
    }

    public int getNewCreditId() {
        return newCreditId;
    }

    public void setNewCreditId(int newCreditId) {
        this.newCreditId = newCreditId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBidRecordId() {
        return bidRecordId;
    }

    public void setBidRecordId(int bidRecordId) {
        this.bidRecordId = bidRecordId;
    }
}
