package com.fenlibao.platform.model;

import java.io.Serializable;

/**
 * Created by Lullaby on 2016/2/18.
 */
public class ApiResponse implements Serializable {

    private Integer code;

    private String message;

    private Integer errcode;

    private String errmsg;

    public ApiResponse() {

    }

    public void success() {
        this.code = Response.RESPONSE_SUCCESS.getCode();
        this.message = Response.RESPONSE_SUCCESS.getMessage();
        this.errcode = null;
        this.errmsg = null;
    }

    public void failure() {
        this.errcode = Response.RESPONSE_FAILURE.getCode();
        this.errmsg = Response.RESPONSE_FAILURE.getMessage();
        this.code = null;
        this.message = null;
    }

    public void setCodeMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.errcode = null;
        this.errmsg = null;
    }

    public void setErrorCodeMsg(Integer errcode, String errmsg) {
        this.code = null;
        this.message = null;
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

}
