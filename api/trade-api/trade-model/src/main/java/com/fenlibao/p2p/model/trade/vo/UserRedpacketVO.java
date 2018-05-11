package com.fenlibao.p2p.model.trade.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zcai on 2016/12/19.
 */
public class UserRedpacketVO {

    //红包使用标类型 期限 有效天数等
    private Integer userRedpacketId;//用户红包ID
    private Integer redpacketId;//红包ID
    private BigDecimal amount;//红包金额
    private Date validDate;//有效期
    private BigDecimal investAmount; //需投资金额
    private Integer investDeadline; //投资期限（单位天）
    private List<BidType_> bidTypes; //标类型

    public Integer getUserRedpacketId() {
        return userRedpacketId;
    }

    public void setUserRedpacketId(Integer userRedpacketId) {
        this.userRedpacketId = userRedpacketId;
    }

    public Integer getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(Integer redpacketId) {
        this.redpacketId = redpacketId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public Integer getInvestDeadline() {
        return investDeadline;
    }

    public void setInvestDeadline(Integer investDeadline) {
        this.investDeadline = investDeadline;
    }

    public List<BidType_> getBidTypes() {
        return bidTypes;
    }

    public void setBidTypes(List<BidType_> bidTypes) {
        bidTypes = bidTypes;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }
}
