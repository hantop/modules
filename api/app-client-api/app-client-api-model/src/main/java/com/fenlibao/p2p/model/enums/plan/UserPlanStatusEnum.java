package com.fenlibao.p2p.model.enums.plan;

/**
 * 用户投资计划状态(1:持有中 2:申请退出 3:已退出)',
 * Created by kris on 2017/3/24.
 */
public enum UserPlanStatusEnum {
    CYZ(1, "持有中"),
    SQTC(2, "申请退出"),
    YTC(3, "已退出")
    ;

    private int code;
    private String name;

    UserPlanStatusEnum(int code, String name) {
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
        return UserPlanStatusEnum.values().length;
    }
}
