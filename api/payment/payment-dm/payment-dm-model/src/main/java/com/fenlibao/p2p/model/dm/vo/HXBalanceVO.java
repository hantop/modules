package com.fenlibao.p2p.model.dm.vo;

import java.math.BigDecimal;

/**
 * Created by zcai on 2016/11/30.
 */
public class HXBalanceVO {

    private BigDecimal balance;//账户余额
    private BigDecimal available;//可用余额
    private BigDecimal frozen;//冻结金额

    public HXBalanceVO() {}

    public HXBalanceVO(BigDecimal balance, BigDecimal available, BigDecimal frozen) {
        this.balance = balance;
        this.available = available;
        this.frozen = frozen;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }
}
