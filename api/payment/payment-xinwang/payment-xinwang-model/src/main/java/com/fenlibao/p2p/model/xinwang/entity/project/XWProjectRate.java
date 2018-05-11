package com.fenlibao.p2p.model.xinwang.entity.project;

import java.math.BigDecimal;

/**
 * t6238
 */
public class XWProjectRate {
    private Integer projectNo;
    /**
     * 成交服务费率
     */
    private BigDecimal transactionServiceRate;
    /**
     * 逾期罚息费率
     */
    private BigDecimal lateChargeRate;
    /**
     * 借款人利率
     */
    private BigDecimal borrowerRate;
    /**
     * 逾期手续费率
     */
    private BigDecimal overdueCommissionRate;
    /**
     * 标加息利率
     */
    private BigDecimal projectRaiseInterestRate;

    /**
     * 利息管理费率
     */
    private BigDecimal interestManagementRate;

    /**
     * 标还款利率
     */
    private BigDecimal repaymentRate;

    public Integer getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(Integer projectNo) {
        this.projectNo = projectNo;
    }

    public BigDecimal getTransactionServiceRate() {
        return transactionServiceRate;
    }

    public void setTransactionServiceRate(BigDecimal transactionServiceRate) {
        this.transactionServiceRate = transactionServiceRate;
    }

    public BigDecimal getLateChargeRate() {
        return lateChargeRate;
    }

    public void setLateChargeRate(BigDecimal lateChargeRate) {
        this.lateChargeRate = lateChargeRate;
    }

    public BigDecimal getBorrowerRate() {
        return borrowerRate;
    }

    public void setBorrowerRate(BigDecimal borrowerRate) {
        this.borrowerRate = borrowerRate;
    }

    public BigDecimal getOverdueCommissionRate() {
        return overdueCommissionRate;
    }

    public void setOverdueCommissionRate(BigDecimal overdueCommissionRate) {
        this.overdueCommissionRate = overdueCommissionRate;
    }

    public BigDecimal getProjectRaiseInterestRate() {
        return projectRaiseInterestRate;
    }

    public void setProjectRaiseInterestRate(BigDecimal projectRaiseInterestRate) {
        this.projectRaiseInterestRate = projectRaiseInterestRate;
    }

    public BigDecimal getInterestManagementRate() {
        return interestManagementRate;
    }

    public void setInterestManagementRate(BigDecimal interestManagementRate) {
        this.interestManagementRate = interestManagementRate;
    }

    public BigDecimal getRepaymentRate() {
        return repaymentRate;
    }

    public void setRepaymentRate(BigDecimal repaymentRate) {
        this.repaymentRate = repaymentRate;
    }
}
