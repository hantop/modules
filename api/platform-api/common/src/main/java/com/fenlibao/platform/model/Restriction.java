package com.fenlibao.platform.model;

import java.io.Serializable;

/**
 * Created by Lullaby on 2016/2/17.
 */
public class Restriction implements Serializable {

    private String appid;

    private String secret;

    public Restriction() {

    }

    public Restriction(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
