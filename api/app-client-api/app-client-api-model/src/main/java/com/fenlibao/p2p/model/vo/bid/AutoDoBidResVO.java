package com.fenlibao.p2p.model.vo.bid;

import java.math.BigDecimal;

/**
 * Created by xiao on 2016/11/15.
 */
public class AutoDoBidResVO {

    private int id;//标ID

    private String name;//借款标题

    private BigDecimal purchaseAmount;//购买金额

    public AutoDoBidResVO() {

    }

    public AutoDoBidResVO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AutoDoBidResVO(int id, String name, BigDecimal purchaseAmount) {
        this.id = id;
        this.name = name;
        this.purchaseAmount = purchaseAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}
