package com.fenlibao.p2p.sms.config;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.sms.config.annotation.PropMap;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2015/8/21.
 */
public class XuanWuConfig {

    @PropMap("XUANWU_USERNAME")
    @NotNull(message = "玄武用戶名不能為空")
    private String username;

    @PropMap("XUANWU_PASSWORD")
    @NotNull(message = "玄武密碼不能為空")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        AES aes = AES.getInstace();
        return aes.decrypt(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
