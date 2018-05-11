package com.fenlibao.p2p.model.enums.user;

/**
 * 实名认证返回结果
 * @author Mingway.Xu
 * @date 2017/4/28 11:13
 */
public enum UserAuthEnum {
    UD_AUTH_MATCH("1", "认证一致"),
    UD_AUTH_UN_MATCH("2","认证不一致"),
    UD_AUTH_NOT_CONTAIN("3", "无结果");

    private String code;
    private String name;

    UserAuthEnum(String code, String name) {
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
        return UserAuthEnum.values().length;
    }
}
