package com.fenlibao.p2p.alidayu.sms.config;

import com.fenlibao.p2p.sms.config.annotation.PropMap;

/**
 * Created by Bogle on 2016/2/26.
 */
public class AlidayuConfig {

    public static final String TYPE = "ALIDAYU";

    @PropMap("ALIDAYU_URL")
    private String url;//url

    @PropMap("ALIDAYU_APP_KEY")
    private String appkey;

    @PropMap("ALIDAYU_SECRET")
    private String secret;

    @PropMap("ALIDAYU_SIGN_NAME")
    private String signName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}
