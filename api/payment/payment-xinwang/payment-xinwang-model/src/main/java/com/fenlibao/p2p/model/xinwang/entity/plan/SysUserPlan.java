package com.fenlibao.p2p.model.xinwang.entity.plan;

import com.fenlibao.p2p.model.xinwang.enums.plan.SysUserPlan_Status;

import java.math.BigDecimal;
import java.util.Date;

/**
 * flb.t_user_plan
 */
public class SysUserPlan {
    private Integer id;
    private Integer userId;
    private Integer planId;
    private BigDecimal investAmount;
    private BigDecimal beinvestAmount;
    private SysUserPlan_Status status;
    private Date exitTime;

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

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getBeinvestAmount() {
        return beinvestAmount;
    }

    public void setBeinvestAmount(BigDecimal beinvestAmount) {
        this.beinvestAmount = beinvestAmount;
    }

    public SysUserPlan_Status getStatus() {
        return status;
    }

    public void setStatus(SysUserPlan_Status status) {
        this.status = status;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }
}
