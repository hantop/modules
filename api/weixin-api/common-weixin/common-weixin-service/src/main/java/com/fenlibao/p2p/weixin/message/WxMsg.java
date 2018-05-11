package com.fenlibao.p2p.weixin.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/17.
 */
public class WxMsg implements Serializable {

    private static final long serialVersionUID = 5773847700443411128L;
    //用户标识
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "openid")
    private String openid;

    //错误编码
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "errcode")
    private int errcode;

    //错误消息
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "errmsg")
    private String errmsg;

    //回话token
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "access_token")
    private String accessToken;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
