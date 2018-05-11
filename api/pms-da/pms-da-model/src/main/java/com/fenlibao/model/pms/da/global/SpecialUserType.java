package com.fenlibao.model.pms.da.global;

/**
 * 特殊账号类型
 * Created by zcai on 2016/8/25.
 */
public enum SpecialUserType {

    ALL(0),
    /**
     * 无新手限制
     */
    WXSXZ(1),
    /**
     * 低于100可投
     */
    DYYBKT(2),
    /**
     * 购买债权转让
     */
    ZQZR(3),
    /**
     * 购买未满计划
     */
    INVEST_PLAN_NOT_FULL(4);

    private int code;

    private SpecialUserType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
