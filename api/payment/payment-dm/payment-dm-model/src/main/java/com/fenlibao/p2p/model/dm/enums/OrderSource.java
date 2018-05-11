package com.fenlibao.p2p.model.dm.enums;

/**
 * 订单来源
 * Created by zcai on 2016/10/23.
 */
public enum OrderSource {

    USER(1),
    PMS(2),
    SYSTEM(3),

    ;

    private int code;

    OrderSource(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
