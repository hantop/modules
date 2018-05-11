package com.fenlibao.p2p.model.entity.bid;


import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 用户投资的计划记录
 * Created by xiao on 2017/3/21.
 */
public class UserPlanInfo {
    private int id;
    private int userId;
    private int planId;
    private BigDecimal investAmount;//投资金额
    private BigDecimal beinvestAmount;//待投资金额(用户在计划里剩余金额包含利息)
    private int status;//状态(1:持有中 2:申请退出 3:已退出)
    private int exitTime;
    private Timestamp createTime;//创建时间
    private Timestamp updateTime;//修改时间

    private Timestamp expireTime;//到期时间
    private int planStatus;
    private int expireDateDiff;
    private int cgMode;//存管标志

    private int cycle;//借款周期
    private String cycleType;//借款周期的单位(d:天 m:月)
    //private BigDecimal firstBeinvestAmount;//初投剩余金额（投资金额-配标金额），不算投资回款金额

    public UserPlanInfo() {
    }

    public UserPlanInfo(int id, int userId, int planId, BigDecimal investAmount, BigDecimal beinvestAmount, int status, int exitTime, Timestamp createTime, Timestamp updateTime, Timestamp expireTime, int planStatus, int expireDateDiff, int cgMode, int cycle, String cycleType) {
        this.id = id;
        this.userId = userId;
        this.planId = planId;
        this.investAmount = investAmount;
        this.beinvestAmount = beinvestAmount;
        this.status = status;
        this.exitTime = exitTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.expireTime = expireTime;
        this.planStatus = planStatus;
        this.expireDateDiff = expireDateDiff;
        this.cgMode = cgMode;
        this.cycle = cycle;
        this.cycleType = cycleType;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public int getExpireDateDiff() {
        return expireDateDiff;
    }

    public void setExpireDateDiff(int expireDateDiff) {
        this.expireDateDiff = expireDateDiff;
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

    public int getExitTime() {
        return exitTime;
    }

    public void setExitTime(int exitTime) {
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

    public int getCgMode() {
        return cgMode;
    }

    public void setCgMode(int cgMode) {
        this.cgMode = cgMode;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }
}
