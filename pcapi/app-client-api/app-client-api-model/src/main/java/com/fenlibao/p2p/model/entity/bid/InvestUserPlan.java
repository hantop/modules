package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 新计划投资记录
 * Created by xiao on 2017/3/24.
 */
public class InvestUserPlan {
    private int id;
    private int userId;
    private int planId;
    private BigDecimal investAmount;//投资金额
    private BigDecimal beinvestAmount;//待投资金额(用户在计划里剩余金额包含利息)
    private int status;//状态(1:持有中 2:申请退出 3:已退出)
    private Timestamp exitTime;
    private Timestamp createTime;//创建时间
    private Timestamp updateTime;//修改时间

    private BigDecimal freezeAmount;//存管账户对应冻结金额

    public InvestUserPlan() {
    }

    public InvestUserPlan(int id, int userId, int planId, BigDecimal investAmount, BigDecimal beinvestAmount, int status, Timestamp exitTime, Timestamp createTime, Timestamp updateTime, BigDecimal freezeAmount) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.investAmount = investAmount;
        this.beinvestAmount = beinvestAmount;
        this.status = status;
        this.exitTime = exitTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.freezeAmount = freezeAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getExitTime() {
        return exitTime;
    }

    public void setExitTime(Timestamp exitTime) {
        this.exitTime = exitTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
