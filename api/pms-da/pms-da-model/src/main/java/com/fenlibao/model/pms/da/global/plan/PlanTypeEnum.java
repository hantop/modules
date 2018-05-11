package com.fenlibao.model.pms.da.global.plan;

/**
 * 投资计划类型(1:月月升计划 2:省心计划)',
 * Created by kris on 2017/3/24.
 */
public enum PlanTypeEnum {
    YYS(1, "月月升计划"),
    SXJH(2, "省心计划")

    ;

    private int code;
    private String name;

    PlanTypeEnum(int code, String name) {
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
        return PlanTypeEnum.values().length;
    }
}
