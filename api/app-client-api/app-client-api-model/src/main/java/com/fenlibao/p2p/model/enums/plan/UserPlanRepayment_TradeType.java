package com.fenlibao.p2p.model.enums.plan;

/**
 */
public enum UserPlanRepayment_TradeType {
    BX(1, "本息"),
    YQFX(2, "逾期罚息"),
    ;

    private int code;
    private String name;

    UserPlanRepayment_TradeType(int code, String name) {
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
        return UserPlanRepayment_TradeType.values().length;
    }
}
