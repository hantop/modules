package com.fenlibao.p2p.weixin.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2015/8/25.
 */
@Component
@ConfigurationProperties(prefix = "fenlibao")
public class ConnectionSettings {

    private String phpHome;
    private String phpAutoLogin;
    private String phpCancelLogin;
    private String serverBind;

    public String getPhpHome() {
        return phpHome;
    }

    public void setPhpHome(String phpHome) {
        this.phpHome = phpHome;
    }

    public String getPhpAutoLogin() {
        return phpAutoLogin;
    }

    public void setPhpAutoLogin(String phpAutoLogin) {
        this.phpAutoLogin = phpAutoLogin;
    }

    public String getPhpCancelLogin() {
        return phpCancelLogin;
    }

    public void setPhpCancelLogin(String phpCancelLogin) {
        this.phpCancelLogin = phpCancelLogin;
    }

    public String getServerBind() {
        return serverBind;
    }

    public void setServerBind(String serverBind) {
        this.serverBind = serverBind;
    }
}
