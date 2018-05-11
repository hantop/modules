package com.fenlibao.p2p.model.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PfBidInfoVo {

    private int bid; // 标id
    private BigDecimal voteAmount; // 可投金额
    private Date successTime; // 满标时间
    private String state; // 状态

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public BigDecimal getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(BigDecimal voteAmount) {
        this.voteAmount = voteAmount;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
