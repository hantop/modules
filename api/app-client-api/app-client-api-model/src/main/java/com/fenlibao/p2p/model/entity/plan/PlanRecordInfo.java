package com.fenlibao.p2p.model.entity.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 3.2版本发布后的计划记录信息
 *
 * @author Mingway.Xu
 * @date 2017/3/23 14:24
 */
public class PlanRecordInfo {
    private int userPlanId;//flb.t_user_plan 用户计划记录id

    private int userId;//投资的用户的id

    private Date investTime;//投资时间

    private int type;//计划类型

    private int isNovice;//新手计划

    private double moIncreaseRate;//月增幅加息

    private double lowRate;//最低利率

    private double investRate;//投资时的年化利率

    private BigDecimal investAmount;//用户投资的本金

    private int loanDate;//借款周期

    private int canQuit;//是否是可以退出的计划

    private int status;//退出状态

    private double raiseRate;//加息

    private double highRate;//最高利率

    private String planTitle;//计划标题

    private double couponRise;//券加息

    private Date interestTime;//起息时间

    private Date nextRepaymentDate;//到期日期

    private int planStatus;//计划的状态

    public int getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(int userPlanId) {
        this.userPlanId = userPlanId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsNovice() {
        return isNovice;
    }

    public void setIsNovice(int isNovice) {
        this.isNovice = isNovice;
    }

    public double getMoIncreaseRate() {
        return moIncreaseRate;
    }

    public void setMoIncreaseRate(double moIncreaseRate) {
        this.moIncreaseRate = moIncreaseRate;
    }

    public double getInvestRate() {
        return investRate;
    }

    public void setInvestRate(double investRate) {
        this.investRate = investRate;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public int getCanQuit() {
        return canQuit;
    }

    public void setCanQuit(int canQuit) {
        this.canQuit = canQuit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLowRate() {
        return lowRate;
    }

    public void setLowRate(double lowRate) {
        this.lowRate = lowRate;
    }

    public int getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(int loanDate) {
        this.loanDate = loanDate;
    }

    public double getRaiseRate() {
        return raiseRate;
    }

    public void setRaiseRate(double raiseRate) {
        this.raiseRate = raiseRate;
    }

    public double getHighRate() {
        return highRate;
    }

    public void setHighRate(double highRate) {
        this.highRate = highRate;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public double getCouponRise() {
        return couponRise;
    }

    public void setCouponRise(double couponRise) {
        this.couponRise = couponRise;
    }

    public Date getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }

    public Date getNextRepaymentDate() {
        return nextRepaymentDate;
    }

    public void setNextRepaymentDate(Date nextRepaymentDate) {
        this.nextRepaymentDate = nextRepaymentDate;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }
}
