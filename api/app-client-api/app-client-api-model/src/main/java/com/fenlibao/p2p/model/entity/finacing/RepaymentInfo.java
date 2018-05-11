package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by laubrence on 2016/3/29.
 */
public class RepaymentInfo {

    int bidId; //标id

    int creditId; //债权id

    int paymentUserId; //付款用户id

    int receiveUserId;//收款用户id

    int tradeType;//交易类型

    String tradeTypeName;//交易类型名称

    int period;//期号

    BigDecimal repaymentAmount;//还款金额

    Date expectedRepaymentDate;//本期应还款日期

    Date actualRepaymentDate;//实际还款日期

    String repaymentStatus;//还款状态（YH:已还款;WH:未还款 ）('WH','HKZ','TQH','YH','DF')

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public int getPaymentUserId() {
        return paymentUserId;
    }

    public void setPaymentUserId(int paymentUserId) {
        this.paymentUserId = paymentUserId;
    }

    public int getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(int receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Date getExpectedRepaymentDate() {
        return expectedRepaymentDate;
    }

    public void setExpectedRepaymentDate(Date expectedRepaymentDate) {
        this.expectedRepaymentDate = expectedRepaymentDate;
    }

    public Date getActualRepaymentDate() {
        return actualRepaymentDate;
    }

    public void setActualRepaymentDate(Date actualRepaymentDate) {
        this.actualRepaymentDate = actualRepaymentDate;
    }

    public String getRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(String repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }
}
