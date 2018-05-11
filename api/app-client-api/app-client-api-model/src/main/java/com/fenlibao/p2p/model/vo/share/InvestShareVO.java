package com.fenlibao.p2p.model.vo.share;

import java.math.BigDecimal;

/**
 * 用户投资对应VO t6250
 *
 * @author Mingway.Xu
 * @date 2017/1/23 17:34
 */
public class InvestShareVO {
    private Integer id ;
    private BigDecimal buyAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }
}
