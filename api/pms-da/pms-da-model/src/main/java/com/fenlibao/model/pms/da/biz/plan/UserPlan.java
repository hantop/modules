package com.fenlibao.model.pms.da.biz.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户投资计划 flb.t_user_plan
 * by：kris 20170325
 */

public class UserPlan extends InvestPlanInfo{
    protected Integer userPlanId;// 用户投资计划id
    protected Integer planId;// 投资计划id
    protected Integer UserId;//用户id
    protected Integer userPlanStatus;//用户投资计划状态 (1:持有中 2:申请退出 3:已退出)
    protected BigDecimal investAmount;//投资金额

    protected Integer passDays;//已过天数
    protected Integer totalDays;//总天数（到期时间 - 计息时间）

    protected Date applyQuitTime;//申请退出时间

    public Integer getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(Integer userPlanId) {
        this.userPlanId = userPlanId;
    }

    @Override
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

    public Date getApplyQuitTime() {
        return applyQuitTime;
    }

    public void setApplyQuitTime(Date applyQuitTime) {
        this.applyQuitTime = applyQuitTime;
    }
}
