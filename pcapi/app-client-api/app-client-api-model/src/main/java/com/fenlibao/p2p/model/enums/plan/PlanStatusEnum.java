package com.fenlibao.p2p.model.enums.plan;

/**
 * 投资计划状态(1:待提交 2:待审核 3:待发布 4: 投资中 5:还款中 6:已结清 7:已作废 8:预发布)
 * Created by kris on 2017/3/24.
 */
public enum PlanStatusEnum {
    DTJ(1, "待提交"),
    DSH(2, "待审核"),
    DFB(3, "待发布"),
    TZZ(4, "出借中"),
    HKZ(5, "还款中"),
    YJQ(6, "已结清"),
    YZF(7, "已作废"),
    YFB(8, "预发布")
    ;

    private int code;
    private String name;

    PlanStatusEnum(int code, String name) {
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
        return PlanStatusEnum.values().length;
    }
}
