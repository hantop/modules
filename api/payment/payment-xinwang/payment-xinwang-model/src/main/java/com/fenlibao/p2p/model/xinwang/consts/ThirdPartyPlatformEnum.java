package com.fenlibao.p2p.model.xinwang.consts;

/**
 * @author zeronx on 2017/12/29 14:49.
 * @version 1.0
 */
public enum  ThirdPartyPlatformEnum {
    SHANGSHANGQIAN(1,"上上签"),
    ;

    private int code;
    private String name;

    ThirdPartyPlatformEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
