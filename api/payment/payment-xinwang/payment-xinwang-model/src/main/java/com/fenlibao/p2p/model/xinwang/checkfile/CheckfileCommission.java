package com.fenlibao.p2p.model.xinwang.checkfile;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/16.
 */
public class CheckfileCommission {
    Date deductMoneyTime;
    String platformNo;
    String orderNo;
    String businessType;
    String sponsorPlatformUserNo;
    String receiverPlatformUserNo;
    BigDecimal amount;
    String currencyType;
    String loadId;
    String remark;
    String requestNo;

    public Date getDeductMoneyTime() {
        return deductMoneyTime;
    }

    public void setDeductMoneyTime(Date deductMoneyTime) {
        this.deductMoneyTime = deductMoneyTime;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSponsorPlatformUserNo() {
        return sponsorPlatformUserNo;
    }

    public void setSponsorPlatformUserNo(String sponsorPlatformUserNo) {
        this.sponsorPlatformUserNo = sponsorPlatformUserNo;
    }

    public String getReceiverPlatformUserNo() {
        return receiverPlatformUserNo;
    }

    public void setReceiverPlatformUserNo(String receiverPlatformUserNo) {
        this.receiverPlatformUserNo = receiverPlatformUserNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }
}
