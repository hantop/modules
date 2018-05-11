package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 验证身份证唯一性
 * @date 2017/5/5 17:03
 */
public enum IDUnique {
    ID_CARD_NO_UNIQUE("ID_CARD_NO_UNIQUE");

    protected final String code;

    IDUnique(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
