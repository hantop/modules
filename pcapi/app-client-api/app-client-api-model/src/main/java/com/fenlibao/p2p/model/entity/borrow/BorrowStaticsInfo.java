package com.fenlibao.p2p.model.entity.borrow;

import java.math.BigDecimal;

/**
 * Created by xiao on 2016/12/28.
 */
public class BorrowStaticsInfo {

    private BigDecimal sumLoanAmount;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal yqsxf;
    private BigDecimal yqfx;

    public BigDecimal getSumLoanAmount() {
        return sumLoanAmount;
    }

    public void setSumLoanAmount(BigDecimal sumLoanAmount) {
        this.sumLoanAmount = sumLoanAmount;
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

    public BigDecimal getYqsxf() {
        return yqsxf;
    }

    public void setYqsxf(BigDecimal yqsxf) {
        this.yqsxf = yqsxf;
    }

    public BigDecimal getYqfx() {
        return yqfx;
    }

    public void setYqfx(BigDecimal yqfx) {
        this.yqfx = yqfx;
    }
}
