package com.fenlibao.p2p.weixin.component;

/**
 * Created by Administrator on 2015/8/17.
 */
public class WeixinInfo {

    private String appId;//应用ID

    private String appSecret;//应用密钥

    private String encodingAESKey;//消息加解密密钥

    private String tokenKey;//微信token

    private String name = "微信二维码";//

    private String suffix = ".jpg";


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }
}
