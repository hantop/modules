package com.fenlibao.p2p.model.enums.bid;

/**
 * 区分手动投资和计划投资
 * Created by xiao on 2017/3/29.
 */
public enum OperationTypeEnum {
    USER("USER", "手动出借"),
    PLAN("PLAN", "计划出借");

    private String code;
    private String name;

    OperationTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static int length() {
        return OperationTypeEnum.values().length;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
