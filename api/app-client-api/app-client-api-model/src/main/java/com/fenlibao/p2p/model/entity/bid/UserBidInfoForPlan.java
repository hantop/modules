package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;

/**
 * Created by xiao on 2017/3/22.
 */
public class UserBidInfoForPlan {
    private int id;
    private String name;
    private BigDecimal rate;
    private BigDecimal loanAmount;
    private BigDecimal voteAmount;
    private int month;
    private int loanDays;
    private String isNoviceBid;
    private String repaymentMode;

    public UserBidInfoForPlan() {

    }

    public UserBidInfoForPlan(int id, String name, BigDecimal rate, BigDecimal loanAmount, BigDecimal voteAmount, int month, int loanDays, String repaymentMode, String isNoviceBid) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.loanAmount = loanAmount;
        this.voteAmount = voteAmount;
        this.month = month;
        this.loanDays = loanDays;
        this.repaymentMode = repaymentMode;
        this.isNoviceBid = isNoviceBid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(BigDecimal voteAmount) {
        this.voteAmount = voteAmount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public String getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(String isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }
}
