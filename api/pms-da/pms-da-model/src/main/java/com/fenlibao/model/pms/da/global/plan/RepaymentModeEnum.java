package com.fenlibao.model.pms.da.global.plan;

/**
 * 标还款方式
 * by：kris
 */
public enum RepaymentModeEnum {
    YCFQ("YCFQ", "一次付清"),
    DEBX("DEBX", "等额本息"),
    MYFX("MYFX", "每月付息"),
    ;

    private String code;
    private String name;

    RepaymentModeEnum(String code, String name) {
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
        return BidTypeEnum.values().length;
    }
}
