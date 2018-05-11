package com.fenlibao.p2p.model.enums.bid;

/**
 * 标类型
 * Created by zcai on 2016/6/24.
 */
public enum BidTypeEnum {
//code为了兼容之前的就不改了,继续使用
    DPRZ("KDB", "店铺融资"),
    CLDY("CDW", "车辆质押"),
    FCDY("FDW", "房产抵押"),
    SB("SB", "散标"),
    YSZK("YSZK", "应收账款担保融资"),
    JH("JH", "计划"),
    ;

    private String code;
    private String name;

    BidTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static int length() {
        return BidTypeEnum.values().length;
    }
}
