package com.fenlibao.p2p.weixin.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fenlibao.p2p.weixin.defines.WeixinStatus;

public class AutoLogin {

    @JsonProperty("openid")
    private String openid;

    @JsonProperty("weixinMsg")
    private String weixinMsg;
    @JsonProperty("weixinStatus")
    private WeixinStatus weixinStatus;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWeixinMsg() {
        return weixinMsg;
    }

    public void setWeixinMsg(String weixinMsg) {
        this.weixinMsg = weixinMsg;
    }

    public WeixinStatus getWeixinStatus() {
        return weixinStatus;
    }

    public void setWeixinStatus(WeixinStatus weixinStatus) {
        this.weixinStatus = weixinStatus;
    }
}