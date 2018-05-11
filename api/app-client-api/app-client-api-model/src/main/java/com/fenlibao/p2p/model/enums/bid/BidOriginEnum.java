package com.fenlibao.p2p.model.enums.bid;

/**
 * 标来源
 * by：kris
 */
public enum BidOriginEnum {
    flb("0001", "分利宝"),
    qqm("0002", "缺钱么"),
    ;

    private String code;
    private String name;

    BidOriginEnum(String code, String name) {
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
        return BidOriginEnum.values().length;
    }
}
