package com.fenlibao.p2p.model.dm.enums;

import com.fenlibao.p2p.model.dm.entity.FundAccount;

/**
 * Created by zcai on 2016/10/19.
 */
public enum FundAccountType {

    WLZH(1),
    SDZH(2)
    ;

    private int code;

    FundAccountType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
