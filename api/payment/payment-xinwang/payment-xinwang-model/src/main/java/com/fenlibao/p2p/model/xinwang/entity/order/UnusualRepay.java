package com.fenlibao.p2p.model.xinwang.entity.order;
/**
 * 非正常还款
 */
public class UnusualRepay {
    Integer id;
    Integer orderId;
    Integer bidId;
    Boolean status;

    public UnusualRepay() {
    }

    public UnusualRepay(Integer orderId, Integer bidId, Boolean status) {
        this.orderId = orderId;
        this.bidId = bidId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
