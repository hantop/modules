package com.fenlibao.p2p.model.entity.creditassignment;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/1.
 */
public class UserCoupons {
    Double scope;

    int tenderId;

    public Double getScope() {
        return scope;
    }

    public void setScope(Double scope) {
        this.scope = scope;
    }

    public int getTenderId() {
        return tenderId;
    }

    public void setTenderId(int tenderId) {
        this.tenderId = tenderId;
    }
}
