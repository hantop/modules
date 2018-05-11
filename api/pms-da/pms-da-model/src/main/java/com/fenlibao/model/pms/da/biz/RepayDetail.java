package com.fenlibao.model.pms.da.biz;

import java.math.BigDecimal;

public class RepayDetail {
    private int bidId;// 标Id
    private BigDecimal totalAmount;// 需还总额
    private BigDecimal principal;// 需还本金
    private BigDecimal interest;// 需还利息
    private BigDecimal penalty;// 违约金
    private BigDecimal balance;// 账户余额
    private BigDecimal defaultInterest;// 罚息
    private String guaranteeAccount;// 担保账户

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getDefaultInterest() {
        return defaultInterest;
    }

    public void setDefaultInterest(BigDecimal defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public String getGuaranteeAccount() {
        return guaranteeAccount;
    }

    public void setGuaranteeAccount(String guaranteeAccount) {
        this.guaranteeAccount = guaranteeAccount;
    }
}
