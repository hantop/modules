package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 鉴权验证类型
 * @date 2017/5/5 17:03
 */
public enum CheckType {
    LIMIT("LIMIT","强制四要素");

    protected final String code;
    protected final String name;

    CheckType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
