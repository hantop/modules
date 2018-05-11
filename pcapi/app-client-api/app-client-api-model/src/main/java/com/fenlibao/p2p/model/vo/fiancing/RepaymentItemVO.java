package com.fenlibao.p2p.model.vo.fiancing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by laubrence on 2016/3/29.
 */
public class RepaymentItemVO {

    int bidId; //标id

    String repaymentAmount;//还款金额

    int tradeType;//金额类型(7001:本金;7002:利息;7004:逾期罚息;7005:提前还

    long repaymentDate;//本期还款日期

    Date expectedRepaymentDate;//本期应还款日期

    Date actualRepaymentDate;//实际还款日期


    String repaymentStatus;//还款状态（YH:已还款;WH:未还款 ）

    int period;//期号

    BigDecimal principal;//待还本金

    BigDecimal accrual;//待还利息

    Date systemDate;//当前系统时间

    int month;//借款期限（月）

    int loanDays;//借款周期（天）

    Integer loanDate;//现在时间距离放款时间

    public String getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(String repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public long getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(long repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(String repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }

    public Date getActualRepaymentDate() {
        return actualRepaymentDate;
    }

    public void setActualRepaymentDate(Date actualRepaymentDate) {
        this.actualRepaymentDate = actualRepaymentDate;
    }

    public Date getExpectedRepaymentDate() {
        return expectedRepaymentDate;
    }

    public void setExpectedRepaymentDate(Date expectedRepaymentDate) {
        this.expectedRepaymentDate = expectedRepaymentDate;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public BigDecimal getAccrual() {
        return accrual;
    }

    public void setAccrual(BigDecimal accrual) {
        this.accrual = accrual;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Integer getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Integer loanDate) {
        this.loanDate = loanDate;
    }
}
