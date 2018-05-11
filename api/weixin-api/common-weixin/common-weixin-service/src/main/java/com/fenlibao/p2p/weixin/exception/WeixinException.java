package com.fenlibao.p2p.weixin.exception;

import com.fenlibao.p2p.weixin.defines.WxCode;

/**
 * Created by Administrator on 2015/6/10.
 */
public class WeixinException extends Exception {

    private WxCode errcode;

    public WeixinException(String message, WxCode errcode) {
        super(message);
        this.errcode = errcode;
    }

    public WxCode getErrcode() {
        return errcode;
    }
}
