package com.fenlibao.p2p.model.xinwang.entity.trade;

import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;

/**
 * @date 2017/5/26 10:03
 */
public class PreTransactionResult {
    String bizType;
    String code;
    String status;
    String errorCode;
    String errorMessage;
    String requestNo;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
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

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public boolean validate() {
        if (this.code.equals(GeneralResponseCode.FAIL.getCode()) || this.status.equals(GeneralStatus.INIT.getStatus())) {
            return false;
        }
        return true;
    }
}
