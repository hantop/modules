package com.fenlibao.model.pms.da.cs.investUser;

import java.math.BigDecimal;

/**
 * @author zeronx on 2018/1/15 17:19.
 * @version 1.0
 */
public class DueInAmount {
    /**
     * 本金
     */
    private BigDecimal principal = BigDecimal.ZERO;

    /**
     * 收益
     */
    private BigDecimal gains = BigDecimal.ZERO;

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getGains() {
        return gains;
    }

    public void setGains(BigDecimal gains) {
        this.gains = gains;
    }
}
