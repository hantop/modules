package com.fenlibao.model.pms.common.global;

/**
 * 资金账户
 * Created by chenzhixuan on 2015/8/24.
 */
public enum SessionKeyEnum {

    /**
     * session的key值管理
     */
    LOG_KEY("log");

    private String key;

    private SessionKeyEnum(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
