package com.fenlibao.p2p.model.xinwang.enums.project;

/**
 * 还款状态
 */
public enum RepaymentPlan_RepayState {
    WH("WH", "未还"),
    HKZ("HKZ", "还款中"),
    YH("YH", "已还"),
    TQH("TQH", "提前还"),
    DF("DF", "垫付"),
    ;

    protected final String code;
    protected final String name;

    RepaymentPlan_RepayState(String code, String name) {
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
