package com.fenlibao.p2p.model.xinwang.entity.common;

import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;

import java.io.Serializable;

/**
 * 基础返回信息
 * @date 2017/5/26 17:33
 */
public class BaseResult implements Serializable{
    Integer code;
    String status;
    Integer errorCode;
    String errorMessage;

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

    public boolean validate() {
        if (this.code.equals(GeneralResponseCode.FAIL.getCode()) || this.status.equals(GeneralStatus.INIT.getStatus())) {
            return false;
        }
        return true;
    }
}
