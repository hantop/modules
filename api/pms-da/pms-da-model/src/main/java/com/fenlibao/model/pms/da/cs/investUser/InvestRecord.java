package com.fenlibao.model.pms.da.cs.investUser;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资记录
 * Created by Administrator on 2017/12/12.
 */
public class InvestRecord {

    private int bidId; // 标id
    private int investId; // 投资记录的id
    private Date investTime; // 投资时间
    private Date sealedTime; // 计息时间
    private  Date expireTime; // 到期时间()
    private BigDecimal rightsMoney; // 债权本金
    private BigDecimal tradeAmount; // 投资金额
    private BigDecimal bidInterest; // 标的利息
    private BigDecimal couponInterest; // 加息券利息/利率
    private BigDecimal bidRaiseInterest; // 标加息利息/利率
    private BigDecimal acutralEarn; // 实际收益
    private String productName; // 产品名称
    private String bidStatus; // 标的状态
    private BigDecimal yearRate; // 年利率
    private int loanDays; // 期限，借款天数
    private int loanMonths; // 期限，借款月数（借款天数为0，则为月数）
    private String paybackWay; // 还款方式
    private String bidType; // 标的类型
    private BigDecimal investCoupon; // 投资券（返现卷金额）

    private int isSold; // 查询条件：0 投资记录，未发生债权转让；1 已发生债权转让

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public Date getSealedTime() {
        return sealedTime;
    }

    public void setSealedTime(Date sealedTime) {
        this.sealedTime = sealedTime;
    }

    public BigDecimal getRightsMoney() {
        return rightsMoney;
    }

    public void setRightsMoney(BigDecimal rightsMoney) {
        this.rightsMoney = rightsMoney;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getBidInterest() {
        return bidInterest;
    }

    public void setBidInterest(BigDecimal bidInterest) {
        this.bidInterest = bidInterest;
    }

    public BigDecimal getCouponInterest() {
        return couponInterest;
    }

    public void setCouponInterest(BigDecimal couponInterest) {
        this.couponInterest = couponInterest;
    }

    public BigDecimal getBidRaiseInterest() {
        return bidRaiseInterest;
    }

    public void setBidRaiseInterest(BigDecimal bidRaiseInterest) {
        this.bidRaiseInterest = bidRaiseInterest;
    }

    public BigDecimal getAcutralEarn() {
        return acutralEarn;
    }

    public void setAcutralEarn(BigDecimal acutralEarn) {
        this.acutralEarn = acutralEarn;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public BigDecimal getYearRate() {
        return yearRate;
    }

    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public int getLoanMonths() {
        return loanMonths;
    }

    public void setLoanMonths(int loanMonths) {
        this.loanMonths = loanMonths;
    }

    public String getPaybackWay() {
        return paybackWay;
    }

    public void setPaybackWay(String paybackWay) {
        this.paybackWay = paybackWay;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public BigDecimal getInvestCoupon() {
        return investCoupon;
    }

    public void setInvestCoupon(BigDecimal investCoupon) {
        this.investCoupon = investCoupon;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getInvestId() {
        return investId;
    }

    public void setInvestId(int investId) {
        this.investId = investId;
    }

    public int getIsSold() {
        return isSold;
    }

    public void setIsSold(int isSold) {
        this.isSold = isSold;
    }
}
