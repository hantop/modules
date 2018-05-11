package com.fenlibao.model.pms.da.statistics.returnedmoney;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 回款信息
 *
 * Created by chenzhixuan on 2016/3/22.
 */
public class ReturnedmoneyInfo {
    private String phonenum;// 投资人手机号
    private String realname;// 投资人姓名
    private String loanTitle;// 借款标题
    private int loanDeadline;// 借款期限
    private int loanDeadlineType;// 借款期限类型，1:天 2：月
    private BigDecimal principal;// 回款本金
    private BigDecimal investingAmount;// 在投金额
    private BigDecimal balance;// 余额，往来账户余额
    private Date shouldReturnDate;// 回款日期
    private Integer status;// 回款状态 0:全部 1:已回款 2:未回款

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public int getLoanDeadline() {
        return loanDeadline;
    }

    public void setLoanDeadline(int loanDeadline) {
        this.loanDeadline = loanDeadline;
    }

    public int getLoanDeadlineType() {
        return loanDeadlineType;
    }

    public void setLoanDeadlineType(int loanDeadlineType) {
        this.loanDeadlineType = loanDeadlineType;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInvestingAmount() {
        return investingAmount;
    }

    public void setInvestingAmount(BigDecimal investingAmount) {
        this.investingAmount = investingAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getShouldReturnDate() {
        return shouldReturnDate;
    }

    public void setShouldReturnDate(Date shouldReturnDate) {
        this.shouldReturnDate = shouldReturnDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}