package com.fenlibao.platform.model;

import java.io.Serializable;

/**
 * Created by Lullaby on 2016/2/17.
 */
public class ApiError extends ApiResponse implements Serializable {

    private Integer errcode;

    private String errmsg;

    public ApiError() {
        this.errcode = Response.RESPONSE_FAILURE.getCode();
        this.errmsg = Response.RESPONSE_FAILURE.getMessage();
    }

    public ApiError(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public void setErrorCodeMsg(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
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
