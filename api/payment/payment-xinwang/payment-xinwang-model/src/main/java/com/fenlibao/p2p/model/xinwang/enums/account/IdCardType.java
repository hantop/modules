package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 身份卡类型
 * @date 2017/5/5 17:03
 */
public enum IdCardType {
    PRC_ID("PRC_ID","身份证"),
    PASSPORT("PASSPORT","护照"),
    COMPATRIOTS_CARD("COMPATRIOTS_CARD","港澳台通行证"),
    PERMANENT_RESIDENCE("PERMANENT_RESIDENCE","外国人永久居住证"),
    ;

    protected final String code;
    protected final String name;

    IdCardType(String code,String name) {
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
