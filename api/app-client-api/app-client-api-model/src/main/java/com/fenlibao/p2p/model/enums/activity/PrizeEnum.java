package com.fenlibao.p2p.model.enums.activity;

/**
 * 年会奖品类型枚举
 */
public enum PrizeEnum {
    HM(1, "小米手机"),
    XJHB(2, "现金红包"),
    IPHONE(3, "苹果手机"),
    ;

    private int code;
    private String name;

    PrizeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static int length() {
        return PrizeEnum.values().length;
    }
}
