package com.fenlibao.p2p.model.api.exception;

import com.fenlibao.p2p.model.api.global.IResponseMessage;

/**
 * (为了和现有的统一这里code使用String)
 * Created by zcai on 2016/10/21.
 */
public class APIException extends RuntimeException {

    protected String code = "500";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public APIException() {
        super();
    }

    public APIException(IResponseMessage resp) {
        super(resp.getMessage());
        this.code = resp.getCode();
    }

    public APIException(String code, String message) {
        super(message);
        this.code = code;
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIException(Throwable cause) {
        super(cause);
    }
}
