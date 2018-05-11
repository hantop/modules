package com.fenlibao.p2p.model.vo.bidinfo;

import java.math.BigDecimal;

/**
 * Created by zcai on 2016/8/25.
 */
public class AutoTenderVO {

    private int bidId;
    private BigDecimal surplusAmount;//剩余可投金额

    public AutoTenderVO() {
    }

    public AutoTenderVO(int bidId, BigDecimal surplusAmount) {
        this.bidId = bidId;
        this.surplusAmount = surplusAmount;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    @Override
    public String toString() {
        return "AutoTenderVO{" +
                "bidId=" + bidId +
                ", surplusAmount=" + surplusAmount +
                '}';
    }
}
