package com.fenlibao.p2p.model.enums.activity;

/**
 *
 * 活动状态枚举
 *
 */
public enum ActivityStatusEnum {
    WKS(0, "未开始"),
    JXZ(1, "进行中"),
    YJS(2, "已结束"),
            ;

    private int code;
    private String name;

    ActivityStatusEnum(int code, String name) {
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
