package com.fenlibao.p2p.weixin.defines;

/**
 * Created by Administrator on 2015/8/20.
 */
public enum APICode {

    SUCCESS(200, "成功"),
    BINDED(201, "已经绑定了免登陆功能，无需再需要绑定"),
    READY_BINDED(202, "该手机号已经在其他微信号开启了免登陆模式"),
    NOTNET(-1, "分利宝后台为开通网络"),
    TIME_FAST(-2, "频繁错误"),
    UNBIND(50000, "该openid未开启自动登陆模式");
    private int code;
    private String errmsg;

    APICode(int code, String errmsg) {
        this.code = code;
        this.errmsg = errmsg;
    }

    public int getCode() {
        return code;
    }

    public String getErrmsg() {
        return errmsg;
    }
}
