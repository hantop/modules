package com.fenlibao.model.pms.da.global;

/**
 * Created by Administrator on 2017/3/21.
 */
public enum InvestPlanStatus {
    AWAIT_SUBMIT(1, "待提交"),
    AWAIT_AUDIT(2, "待审核"),
    AWAIT_RELEASE(3, "待发布"),
    INVESTING(4, "投资中"),
    REFUNDING(5, "还款中"),
    SETTLED(6, "已结清"),
    INVALID(7, "已作废"),
    ADVANCE_RELEASE(8, "预发布"),
    FLOWED (9, "已流计划")
    ;
    private int key;
    private String value;

    InvestPlanStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
