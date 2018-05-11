package com.fenlibao.service.pms.idmt.error;

/**
 * Created by Bogle on 2016/1/26.
 */
public class AccountException extends RuntimeException {

    private int code;

    public AccountException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
