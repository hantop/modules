package com.fenlibao.p2p.weixin.defines;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/8.
 */
public class OauthDefines<T> implements Serializable{

    private WxCode code;
    private String message;
    private T data;

    public OauthDefines(WxCode code) {
        this.code = code;
        this.message = code.getErrmsg();
    }

    public OauthDefines(WxCode code, T data) {
        this.code = code;
        this.data = data;
    }

    public WxCode getCode() {
        return code;
    }

    public void setCode(WxCode code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
