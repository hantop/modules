package com.fenlibao.p2p.model.xinwang.entity.project;

import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;

import java.math.BigDecimal;
import java.util.Date;

/**
 * t6252
 */
public class XWRepaymentPlan {
    private Integer id;
    private Integer projectId;
    /**
     * 付款用户T6110.F01
     */
    private Integer payerId;
    /**
     * 收款用户T6110.F01
     */
    private Integer payeeId;
    private Integer feeType;
    private Integer term;
    private BigDecimal amount;
    /**
     * 应还日期
     */
    private Date dueDate;
    private RepaymentPlan_RepayState repayState;
    private Date actualRepayTime;
    /**
     * 债权id
     */
    private Integer creditId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 付款用户T6110.F01
     */
    public Integer getPayerId() {
        return payerId;
    }

    public void setPayerId(Integer payerId) {
        this.payerId = payerId;
    }

    /**
     * 收款用户T6110.F01
     */
    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public RepaymentPlan_RepayState getRepayState() {
        return repayState;
    }

    public void setRepayState(RepaymentPlan_RepayState repayState) {
        this.repayState = repayState;
    }

    public Date getActualRepayTime() {
        return actualRepayTime;
    }

    public void setActualRepayTime(Date actualRepayTime) {
        this.actualRepayTime = actualRepayTime;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }
}
