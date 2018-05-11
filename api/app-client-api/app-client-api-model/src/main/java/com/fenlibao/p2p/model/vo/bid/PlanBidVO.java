package com.fenlibao.p2p.model.vo.bid;

import java.math.BigDecimal;

/**
 * Created by xiao on 2017/02/07.
 */
public class PlanBidVO {

    private int bidId;//标ID

    private String bidName;//借款标题

    private BigDecimal purchaseAmount;//购买金额

    public PlanBidVO() {

    }

    public PlanBidVO(int bidId, String bidName, BigDecimal purchaseAmount) {
        this.bidId = bidId;
        this.bidName = bidName;
        this.purchaseAmount = purchaseAmount;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}
