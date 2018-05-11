package com.fenlibao.p2p.model.xinwang.vo;

import java.math.BigDecimal;

/**
 * 还款预算
 */
public class RepayBudgetVO {
    public String projectName;
    /**
     * 还款总金额
     */
    public BigDecimal totalAmount=BigDecimal.ZERO;
    /**
     * 本金
     */
    public BigDecimal principal=BigDecimal.ZERO;
    /**
     * 利息
     */
    public BigDecimal interest=BigDecimal.ZERO;

    /**
     * 利息管理费
     */
    public BigDecimal interestManagementFee=BigDecimal.ZERO;

    /**
     * 逾期罚息
     */
    public BigDecimal overduePenalty=BigDecimal.ZERO;
    /**
     * 提前还款违约金
     */
    public BigDecimal prepayPenalty=BigDecimal.ZERO;
    /**
     * 逾期手续费
     */
    public BigDecimal overdueCommission=BigDecimal.ZERO;
    /**
     * 余额
     */
    public BigDecimal balance=BigDecimal.ZERO;
    /**
     * 担保人
     */
    public String guaranteeName;

    /**
     *总的平台服务费
     */
    public BigDecimal serviceFee=BigDecimal.ZERO;

    /**
     *分期剩余的平台服务费
     */
    public BigDecimal serviceFeeNotPay=BigDecimal.ZERO;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public BigDecimal getOverduePenalty() {
        return overduePenalty;
    }

    public void setOverduePenalty(BigDecimal overduePenalty) {
        this.overduePenalty = overduePenalty;
    }

    public BigDecimal getPrepayPenalty() {
        return prepayPenalty;
    }

    public void setPrepayPenalty(BigDecimal prepayPenalty) {
        this.prepayPenalty = prepayPenalty;
    }

    public BigDecimal getOverdueCommission() {
        return overdueCommission;
    }

    public void setOverdueCommission(BigDecimal overdueCommission) {
        this.overdueCommission = overdueCommission;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getServiceFeeNotPay() {
        return serviceFeeNotPay;
    }

    public void setServiceFeeNotPay(BigDecimal serviceFeeNotPay) {
        this.serviceFeeNotPay = serviceFeeNotPay;
    }

    public BigDecimal getInterestManagementFee() {
        return interestManagementFee;
    }

    public void setInterestManagementFee(BigDecimal interestManagementFee) {
        this.interestManagementFee = interestManagementFee;
    }
}
