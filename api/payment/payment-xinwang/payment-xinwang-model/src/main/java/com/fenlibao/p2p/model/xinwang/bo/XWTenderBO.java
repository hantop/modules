package com.fenlibao.p2p.model.xinwang.bo;

import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderEntity;

import java.math.BigDecimal;

/**
 *  异步投资业务类
 */
public class XWTenderBO extends XWTenderEntity {
    BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
