package com.fenlibao.p2p.model.xinwang.entity.pay;

import com.fenlibao.p2p.model.xinwang.enums.common.CGModeEnum;
import com.fenlibao.p2p.model.xinwang.enums.pay.WithdrawApplyStatus;
import com.fenlibao.p2p.model.xinwang.enums.pay.WithdrawArrival;

import java.math.BigDecimal;
import java.util.Date;

/**
 * t6130
 */
public class SysWithdrawApply {
    private Integer id;
    private Integer userId;
    private Integer bankId;
    private BigDecimal amount;
    private BigDecimal commissionReceivable;
    private BigDecimal paidInCommission;
    private Date createTime;
    private WithdrawApplyStatus status;
    private Date grantTime;
    private WithdrawArrival arrival;
    private CGModeEnum cgMode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCommissionReceivable() {
        return commissionReceivable;
    }

    public void setCommissionReceivable(BigDecimal commissionReceivable) {
        this.commissionReceivable = commissionReceivable;
    }

    public BigDecimal getPaidInCommission() {
        return paidInCommission;
    }

    public void setPaidInCommission(BigDecimal paidInCommission) {
        this.paidInCommission = paidInCommission;
    }

    public WithdrawApplyStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawApplyStatus status) {
        this.status = status;
    }

    public WithdrawArrival getArrival() {
        return arrival;
    }

    public void setArrival(WithdrawArrival arrival) {
        this.arrival = arrival;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(Date grantTime) {
        this.grantTime = grantTime;
    }

    public CGModeEnum getCgMode() {
        return cgMode;
    }

    public void setCgMode(CGModeEnum cgMode) {
        this.cgMode = cgMode;
    }
}
