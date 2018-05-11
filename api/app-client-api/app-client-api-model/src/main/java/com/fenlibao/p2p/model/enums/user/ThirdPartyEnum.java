package com.fenlibao.p2p.model.enums.user;

/**
 * 第三方平台
 * by：kris
 */
public enum ThirdPartyEnum {
    shangshangqian("SSQ","上上签"),
    ;

    private String code;
    private String name;

    ThirdPartyEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static int length() {
        return ThirdPartyEnum.values().length;
    }
}
