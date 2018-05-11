package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by xiao on 2017/2/8.
 */
public class PlanRecord {
    private int id;//投资id
    private int userId;//投资用户id
    private int planId;// 所投计划id
    private BigDecimal amount;// 投资总计金额
    private Timestamp createTime;// 计划创建时间
    private Timestamp updateTime;//  计划更新时间

    public PlanRecord() {
    }

    public PlanRecord(int id, int userId, int planId, BigDecimal amount, Timestamp createTime, Timestamp updateTime) {

        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.amount = amount;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
