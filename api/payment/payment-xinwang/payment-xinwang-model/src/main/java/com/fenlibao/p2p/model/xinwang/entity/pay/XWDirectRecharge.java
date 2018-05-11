package com.fenlibao.p2p.model.xinwang.entity.pay;

import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;

import java.math.BigDecimal;

/**
 * 新网直连充值返回参数
 *
 * @date 2017/6/22 11:09
 */
public class XWDirectRecharge {
    String requestNo;
    String code;
    String status;
    String errorCode;
    String errorMessage;
    String rechargeStatus;
    String platformUserNo;
    BigDecimal amount;
    BigDecimal commission;
    String payCompany;
    String rechargeWay;
    String bankcode;
    String payMobile;
    String transactionTime;
    String channelErrorCode;
    String channelErrorMess;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getPayCompany() {
        return payCompany;
    }

    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }

    public String getRechargeWay() {
        return rechargeWay;
    }

    public void setRechargeWay(String rechargeWay) {
        this.rechargeWay = rechargeWay;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getPayMobile() {
        return payMobile;
    }

    public void setPayMobile(String payMobile) {
        this.payMobile = payMobile;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getChannelErrorCode() {
        return channelErrorCode;
    }

    public void setChannelErrorCode(String channelErrorCode) {
        this.channelErrorCode = channelErrorCode;
    }

    public String getChannelErrorMess() {
        return channelErrorMess;
    }

    public void setChannelErrorMess(String channelErrorMess) {
        this.channelErrorMess = channelErrorMess;
    }

    public boolean validate() {
        if (GeneralResponseCode.FAIL.getCode().equals(this.code) || GeneralStatus.INIT.getStatus().equals(this.status)) {
            return false;
        }
        return true;
    }
}
