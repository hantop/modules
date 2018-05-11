package com.fenlibao.p2p.model.xinwang.entity.trade;

import java.math.BigDecimal;

/**
 * 统计计划收款人回款金额时用到
 */
public class SysPlanPayeeReceivePayment {
    private Integer payeeId;
    private Integer feeType;
    private BigDecimal amount;

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
