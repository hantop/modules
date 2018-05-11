package com.fenlibao.p2p.model.xinwang.entity.pay;

import java.math.BigDecimal;

/**
 * flb.t_xw_funds_transfer
 * @date 2017/7/1 11:12
 */
public class XWFundsTransfer {
    private Integer id;
    private String requestNo;
    private String platformUserNo;
    private String otherUserNo;
    private BigDecimal income;
    private BigDecimal outcome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public String getOtherUserNo() {
        return otherUserNo;
    }

    public void setOtherUserNo(String otherUserNo) {
        this.otherUserNo = otherUserNo;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutcome() {
        return outcome;
    }

    public void setOutcome(BigDecimal outcome) {
        this.outcome = outcome;
    }
}
