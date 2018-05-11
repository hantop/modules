package com.fenlibao.p2p.model.xinwang.checkfile;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/16.
 */
public class CheckfileTransaction {
    Date transactionTime;
    String platformNo;
    String requestNo;
    String orderNo;
    String businessType;
    BigDecimal amount;
    BigDecimal interestAmount;
    String currencyType;
    String sponsorUserId;
    String sponsorPlatformUserNo;
    String receiverUserId;
    String receiverPlatformUserNo;
    String loadId;
    String oldOrderNo;
    String remark;
    BigDecimal shareRights;
    String customDefinedParam;

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getPlatformNo() {
        return platformNo;
    }

    public void setPlatformNo(String platformNo) {
        this.platformNo = platformNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }



    public String getSponsorPlatformUserNo() {
        return sponsorPlatformUserNo;
    }

    public void setSponsorPlatformUserNo(String sponsorPlatformUserNo) {
        this.sponsorPlatformUserNo = sponsorPlatformUserNo;
    }

    public String getSponsorUserId() {
        return sponsorUserId;
    }

    public void setSponsorUserId(String sponsorUserId) {
        this.sponsorUserId = sponsorUserId;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getReceiverPlatformUserNo() {
        return receiverPlatformUserNo;
    }

    public void setReceiverPlatformUserNo(String receiverPlatformUserNo) {
        this.receiverPlatformUserNo = receiverPlatformUserNo;
    }

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    public String getOldOrderNo() {
        return oldOrderNo;
    }

    public void setOldOrderNo(String oldOrderNo) {
        this.oldOrderNo = oldOrderNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getShareRights() {
        return shareRights;
    }

    public void setShareRights(BigDecimal shareRights) {
        this.shareRights = shareRights;
    }

    public String getCustomDefinedParam() {
        return customDefinedParam;
    }

    public void setCustomDefinedParam(String customDefinedParam) {
        this.customDefinedParam = customDefinedParam;
    }
}
