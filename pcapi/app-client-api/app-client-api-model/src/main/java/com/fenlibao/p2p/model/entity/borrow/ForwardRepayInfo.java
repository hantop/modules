package com.fenlibao.p2p.model.entity.borrow;

import java.math.BigDecimal;
import java.util.Date;


public class ForwardRepayInfo {

    private Integer loanId;
    private Date currentDate;
    private Date starttime;
    private Date endtime;
    private int loanCycleForMonth;
    private int loanCycleForDay;
    private int minTerm;
    private int term;
    private int typeId;
    private BigDecimal amount;
    private BigDecimal sybj;
    private BigDecimal loanRate;
    private BigDecimal currentPrincipal;

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getCurrentPrincipal() {
        return currentPrincipal;
    }

    public void setCurrentPrincipal(BigDecimal currentPrincipal) {
        this.currentPrincipal = currentPrincipal;
    }

    public BigDecimal getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }

    public BigDecimal getSybj() {
        return sybj;
    }

    public void setSybj(BigDecimal sybj) {
        this.sybj = sybj;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getMinTerm() {
        return minTerm;
    }

    public void setMinTerm(int minTerm) {
        this.minTerm = minTerm;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getLoanCycleForMonth() {
        return loanCycleForMonth;
    }

    public void setLoanCycleForMonth(int loanCycleForMonth) {
        this.loanCycleForMonth = loanCycleForMonth;
    }

    public int getLoanCycleForDay() {
        return loanCycleForDay;
    }

    public void setLoanCycleForDay(int loanCycleForDay) {
        this.loanCycleForDay = loanCycleForDay;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
