package com.fenlibao.p2p.model.vo.user;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/1/19.
 */
public class AccountNoVO {
    private Integer userId;

    private String accountNo;//e账户卡号

    private BigDecimal available;//可用余额

    private BigDecimal frozen;//冻结金额

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
