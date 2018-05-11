package com.fenlibao.p2p.model.xinwang.enums.project;

/**
 * 标的还款类型
 */
public enum RepaymentWay {
    DEBX("FIXED_PAYMENT_MORTGAGE", "等额本息"),
    MYFX("FIRSEINTREST_LASTPRICIPAL", "先息后本"),
    YCFQ("ONE_TIME_SERVICING", "一次性还本付息"),
    DEBJ("FIXED_BASIS_MORTGAGE", "等额本金"),
    ;

    protected final String code;
    protected final String name;

    RepaymentWay(String code, String name) {
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
