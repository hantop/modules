package com.fenlibao.p2p.model.entity.borrow;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiao on 2016/12/27.
 */
public class BorrowInfo {
    private String collectionId;
    private String bid;//借款标ID
    private String loanRecordTitle;
    private BigDecimal borrowRate;
    private Integer loanCycle;
    private String loanUnit;
    private BigDecimal loanAmount;
    private Date refundDay;
    private Integer totalPeriods;//总期数
    private Integer restPeriods;//剩余期数
    private String loanStatus;
    private Integer distanceRefund;
    private BigDecimal repayAmount;
    private Integer term;//当前期数
    private Integer loanDate;
    private BigDecimal refundAmount;//本期应还金额

    private int isInProgress;//是否在处理中，针对华兴需要一段处理时间

    public int getIsInProgress() {
        return isInProgress;
    }

    public void setIsInProgress(int isInProgress) {
        this.isInProgress = isInProgress;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getLoanRecordTitle() {
        return loanRecordTitle;
    }

    public void setLoanRecordTitle(String loanRecordTitle) {
        this.loanRecordTitle = loanRecordTitle;
    }

    public BigDecimal getBorrowRate() {
        return borrowRate;
    }

    public void setBorrowRate(BigDecimal borrowRate) {
        this.borrowRate = borrowRate;
    }

    public Integer getLoanCycle() {
        return loanCycle;
    }

    public void setLoanCycle(Integer loanCycle) {
        this.loanCycle = loanCycle;
    }

    public String getLoanUnit() {
        return loanUnit;
    }

    public void setLoanUnit(String loanUnit) {
        this.loanUnit = loanUnit;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getRefundDay() {
        return refundDay;
    }

    public void setRefundDay(Date refundDay) {
        this.refundDay = refundDay;
    }

    public Integer getTotalPeriods() {
        return totalPeriods;
    }

    public void setTotalPeriods(Integer totalPeriods) {
        this.totalPeriods = totalPeriods;
    }

    public Integer getRestPeriods() {
        return restPeriods;
    }

    public void setRestPeriods(Integer restPeriods) {
        this.restPeriods = restPeriods;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public Integer getDistanceRefund() {
        return distanceRefund;
    }

    public void setDistanceRefund(Integer distanceRefund) {
        this.distanceRefund = distanceRefund;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Integer loanDate) {
        this.loanDate = loanDate;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}
