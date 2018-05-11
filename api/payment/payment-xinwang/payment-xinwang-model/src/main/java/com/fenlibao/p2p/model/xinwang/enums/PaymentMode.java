package com.fenlibao.p2p.model.xinwang.enums;

/**
 * 支付方式
 * @author Mingway.Xu
 * @date 2017/5/11 9:39
 */
public enum PaymentMode {
    WEB("WEB", "网银" ,0),
    SWIFT("SWIFT", "快捷支付",1),
    BANK("BANK", "转账充值",2),
    BACKROLL("BACKROLL", "资金回退充值",3),
    PROXY("PROXY", "自动充值", 4),;

    protected final String code;
    protected final String name;
    protected final int index;

    PaymentMode(String code, String name,int index) {
        this.code = code;
        this.name = name;
        this.index = index;
    }

    public static PaymentMode parse(int index) {    //手写的从int到enum的转换函数
        for (PaymentMode item : PaymentMode.values()) {
            if (item.getIndex() == index) {
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
