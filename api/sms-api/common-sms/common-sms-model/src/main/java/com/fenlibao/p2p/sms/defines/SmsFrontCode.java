package com.fenlibao.p2p.sms.defines;

/**
 * Created by Administrator on 2015/9/16.
 */
public enum SmsFrontCode {
    ERROR_CODE_$10000(-10000, "客服端，登陆", "用户名或密码错误");

    private int errorcode;
    private String errmsg;
    private String source;
    SmsFrontCode(int errorcode, String source, String errmsg) {
        this.errorcode = errorcode;
        this.errmsg = errmsg;
        this.source = source;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getSource() {
        return source;
    }
}
