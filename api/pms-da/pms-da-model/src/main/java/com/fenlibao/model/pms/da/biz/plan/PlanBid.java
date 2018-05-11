package com.fenlibao.model.pms.da.biz.plan;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/22 13:38.
 * @version 1.0
 */
public class PlanBid implements Serializable {
    private Integer bidId; // 标id
    private String status; // 标状态
    private Integer userId; // 借款人Id
    private BigDecimal voteAmount; // 可投金额

    public PlanBid() {
    }

    public PlanBid(Integer bidId, String status, Integer userId, BigDecimal voteAmount) {
        this.bidId = bidId;
        this.status = status;
        this.userId = userId;
        this.voteAmount = voteAmount;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(BigDecimal voteAmount) {
        this.voteAmount = voteAmount;
    }
}
