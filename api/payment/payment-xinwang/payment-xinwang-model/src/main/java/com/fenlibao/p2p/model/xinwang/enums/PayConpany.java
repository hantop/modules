package com.fenlibao.p2p.model.xinwang.enums;

/**
 * 支付公司
 * @author Mingway.Xu
 * @date 2017/5/10 16:49
 */
public enum PayConpany {
    YEEPAY("YEEPAY"),
    BAOFOO("BAOFOO"),
    LIANLIAN("LIANLIAN");

    protected final String code;

    PayConpany(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PayConpany parse(String code) {
        PayConpany result = YEEPAY;
        for (PayConpany payConpany : PayConpany.values()) {
            if (payConpany.name().equals(code)) {
                result =  payConpany;
            }
        }
        return result;
    }
}
