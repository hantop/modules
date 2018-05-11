package com.fenlibao.p2p.model.xinwang.entity.trade;

import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @date 2017/5/22 20:06
 */
public class SyncTransactionResult implements Serializable {
    Integer code;
    String status;
    Integer errorCode;
    String errorMessage;
    String transactionStatus;
    String requestNo;
    Date createTime;
    Date transactionTime;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public boolean validate() {
        if (GeneralResponseCode.SUCCESS.getCode().equals(this.code)
                && GeneralStatus.SUCCESS.getStatus().equals(this.status)
                && GeneralStatus.SUCCESS.getStatus().equals(this.transactionStatus)) {
            return true;
        }
        return false;
    }
}
