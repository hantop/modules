package com.fenlibao.p2p.sms.defines;

/**
 * Created by Administrator on 2015/9/6.
 */
public enum ResultCode {

    ERROR_CODE_$9020(-9020, "客服端，发送业务", "发送短信手机号不能为空"),
    ERROR_CODE_$9021(-9021, "客服端，发送业务", "目前不支持群发功能");

    private int errorcode;
    private String errmsg;
    private String source;

    ResultCode(int errorcode, String source, String errmsg) {
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
