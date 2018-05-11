package com.fenlibao.p2p.model.dm.entity;

import java.math.BigDecimal;

/**
 * Created by zcai on 2016/10/19.
 */
public class FundAccount {

    private Integer id;
    private Integer userId;
    private Integer type;//账户类型(1=往来账户，2=锁定账户)
    private BigDecimal balance;//余额

    public FundAccount() {}

    public FundAccount(int userId, int type) {
        this.userId = userId;
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
