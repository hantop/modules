package com.fenlibao.p2p.model.entity.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户投资计划 flb.t_user_plan
 * by：kris 20170325
 */

public class UserPlan {
    private int userPlanId;// 用户投资计划id
    private Integer planId;// 投资计划id
    private Integer UserId;//用户id
    private Integer userPlanStatus;//用户投资计划状态 (1:持有中 2:申请退出 3:已退出)
    private BigDecimal investAmount;//投资金额
    private Date bearrateTime;//起息时间
    private BigDecimal totalRate;//总年化率
    private Integer passDays;//已过天数
    private Integer totalDays;//总天数（到期时间 - 计息时间）
    private String cycleType;//enum('d','m') 借款周期类型（按天/按月）
    private Integer cycle;//借款周期
    private Date expireTime;//到期时间
    private Date applyQuitTime;//申请退出时间

    public int getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(int userPlanId) {
        this.userPlanId = userPlanId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getUserPlanStatus() {
        return userPlanStatus;
    }

    public void setUserPlanStatus(Integer userPlanStatus) {
        this.userPlanStatus = userPlanStatus;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Date getBearrateTime() {
        return bearrateTime;
    }

    public void setBearrateTime(Date bearrateTime) {
        this.bearrateTime = bearrateTime;
    }

    public BigDecimal getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(BigDecimal totalRate) {
        this.totalRate = totalRate;
    }

    public Integer getPassDays() {
        return passDays;
    }

    public void setPassDays(Integer passDays) {
        this.passDays = passDays;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getApplyQuitTime() {
        return applyQuitTime;
    }

    public void setApplyQuitTime(Date applyQuitTime) {
        this.applyQuitTime = applyQuitTime;
    }
}
