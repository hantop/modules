package com.fenlibao.p2p.sms.config.defines;

import com.fenlibao.p2p.sms.config.annotation.PropMap;

/**
 * Created by Administrator on 2015/8/29.
 */
public class SmsConfig {

    public static final String TYPE = "SMS_CONFIG";

    @PropMap("DEFAULT_SERVICE")
    private String defaultService;

    @PropMap("COUNT")
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDefaultService() {
        return defaultService;
    }

    public void setDefaultService(String defaultService) {
        this.defaultService = defaultService;
    }
}
