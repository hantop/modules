package com.fenlibao.p2p.sms.config;

import com.fenlibao.p2p.sms.config.annotation.PropMap;

/**
 * Created by Administrator on 2015/9/7.
 */
public class UserVerify {

    public static final String TYPE = "USERINFO";

    @PropMap("USERNAME")
    private String username;

    @PropMap("PASSWORD")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
