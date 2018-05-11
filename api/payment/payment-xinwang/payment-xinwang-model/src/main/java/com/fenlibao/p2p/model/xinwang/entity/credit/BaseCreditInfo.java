package com.fenlibao.p2p.model.xinwang.entity.credit;

import java.math.BigDecimal;

/**
 * @date 2017/7/12 9:34
 */
public class BaseCreditInfo {
    Integer creditId;//T6251.F01
    Integer userId;//受让用户
    Integer debtUserId;//出让用户
    Integer bidId;//标id
    BigDecimal sourceAmount;//原始债权金额

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDebtUserId() {
        return debtUserId;
    }

    public void setDebtUserId(Integer debtUserId) {
        this.debtUserId = debtUserId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
    }
}
