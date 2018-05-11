package com.fenlibao.p2p.model.xinwang.entity.order;

import java.math.BigDecimal;

/**
 * 系统投标订单
 * @date 2017/6/1 9:43
 */
public class SysTenderOrder {
    int orderId;
    int userId;
    int bidId;
    BigDecimal tenderAmount;
    int recordId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getTenderAmount() {
        return tenderAmount;
    }

    public void setTenderAmount(BigDecimal tenderAmount) {
        this.tenderAmount = tenderAmount;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}
