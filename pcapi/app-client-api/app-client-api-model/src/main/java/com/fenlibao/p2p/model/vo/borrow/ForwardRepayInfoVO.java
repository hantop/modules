package com.fenlibao.p2p.model.vo.borrow;

import java.math.BigDecimal;


public class ForwardRepayInfoVO {
    /**
     * 还款标ID
     */
    private int loanId;

    /**
     * 提前还款总需
     */
    private BigDecimal loanTotalMoney = new BigDecimal(0);

    /**
     * 当期应还本金
     */
    private BigDecimal currentPrincipal = new BigDecimal(0);

    /**
     * 当期应还利息
     */
    private BigDecimal currentInterest = new BigDecimal(0);

    /**
     * 剩余本金
     */
    private BigDecimal sybj = new BigDecimal(0);

    /**
     * 提前还款手续费
     */
    private BigDecimal loanManageAmount = new BigDecimal(0);

    /**
     * 违约金
     */
    private BigDecimal loanPenalAmount = new BigDecimal(0);

    /**
     * 当前期号
     */
    private int term;

    /**
     * 可用余额
     * @return
     */
    private BigDecimal available;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getLoanTotalMoney() {
        return loanTotalMoney;
    }

    public void setLoanTotalMoney(BigDecimal loanTotalMoney) {
        this.loanTotalMoney = loanTotalMoney;
    }

    public BigDecimal getCurrentPrincipal() {
        return currentPrincipal;
    }

    public void setCurrentPrincipal(BigDecimal currentPrincipal) {
        this.currentPrincipal = currentPrincipal;
    }

    public BigDecimal getCurrentInterest() {
        return currentInterest;
    }

    public void setCurrentInterest(BigDecimal currentInterest) {
        this.currentInterest = currentInterest;
    }

    public BigDecimal getSybj() {
        return sybj;
    }

    public void setSybj(BigDecimal sybj) {
        this.sybj = sybj;
    }

    public BigDecimal getLoanManageAmount() {
        return loanManageAmount;
    }

    public void setLoanManageAmount(BigDecimal loanManageAmount) {
        this.loanManageAmount = loanManageAmount;
    }

    public BigDecimal getLoanPenalAmount() {
        return loanPenalAmount;
    }

    public void setLoanPenalAmount(BigDecimal loanPenalAmount) {
        this.loanPenalAmount = loanPenalAmount;
    }


    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }
}
