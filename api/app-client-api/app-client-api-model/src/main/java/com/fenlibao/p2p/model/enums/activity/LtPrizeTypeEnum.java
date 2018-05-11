package com.fenlibao.p2p.model.enums.activity;

/**
 *
 * lt_lottery_prize类型枚举
 * Created by xiao on 2017/4/25.
 */
public enum LtPrizeTypeEnum {
    WZJ(0, "未中奖"),
    SHJL(1, "实物奖励"),
    TYJ(2, "体验金"),
    FXHB(3, "返现红包"),
    JF(4, "积分"),
    JXQ(5, "加息券"),
    XJHB(6, "现金红包"),
    ;

    private int code;
    private String name;

    LtPrizeTypeEnum(int code, String name) {
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
