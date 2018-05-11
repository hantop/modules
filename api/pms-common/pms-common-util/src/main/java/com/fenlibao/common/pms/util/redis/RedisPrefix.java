package com.fenlibao.common.pms.util.redis;

/**
 * Redis命名前缀统一规范
 * Created by Lullaby on 2015-11-27 18:12
 */
public enum RedisPrefix {

    LOGIN_CAPTCHA("login:captcha:");

    private String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

}
