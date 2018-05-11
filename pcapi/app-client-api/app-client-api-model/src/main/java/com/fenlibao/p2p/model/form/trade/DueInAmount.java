package com.fenlibao.p2p.model.form.trade;

import java.math.BigDecimal;

/**
 * 待收金额
 *
 * @author yangzengcai
 * @date 2015年10月13日
 */
public class DueInAmount {

    /**
     * 本金
     */
    private BigDecimal principal;

    /**
     * 利息
     */
    private BigDecimal interest;

    private BigDecimal others;

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getOthers() {
        return others;
    }

    public void setOthers(BigDecimal others) {
        this.others = others;
    }
}
