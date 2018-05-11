package com.fenlibao.p2p.model.vo.plan;

import java.math.BigDecimal;

/**
 * 用户投资的计划
 * @author Mingway.Xu
 * @date 2017/4/7 16:14
 */
public class PlanFinacingVO {
    /*计划Id*/
    int planId;

    /*计划记录id*/
    int recordId;

    /*计划名称*/
    String planTitle;

    boolean jxFlag; //是否已加息

    int newPlan;//新计划

    double monthYearYield;//当月年利率

    /*借款周期（单位：月）*/
    int loanMonths;

    /*借款周期（单位：天）*/
    int loanDays;

    /*预期收益*/
    BigDecimal expectedProfit;

    Long expiredDate;//计划退出成功日期

    Long applyTime;//申请退出日期

    /*投资状态*/
    int investStatus;

    int planCanQuit;//计划是否可以退出

    /*投资金额*/
    BigDecimal voteAmount;

    Long purchaseTime;//购买时间

    /*加息标加息利率*/
    BigDecimal bidInterestRise;

    Long interestTime;//起息时间

    Integer holdDays;//持有时间

    String novicePlan;//新手计划

    String planStatus;//计划状态

    /*投资金额*/
    BigDecimal investAmount;

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public boolean isJxFlag() {
        return jxFlag;
    }

    public void setJxFlag(boolean jxFlag) {
        this.jxFlag = jxFlag;
    }

    public int getNewPlan() {
        return newPlan;
    }

    public void setNewPlan(int newPlan) {
        this.newPlan = newPlan;
    }

    public double getMonthYearYield() {
        return monthYearYield;
    }

    public void setMonthYearYield(double monthYearYield) {
        this.monthYearYield = monthYearYield;
    }

    public int getLoanMonths() {
        return loanMonths;
    }

    public void setLoanMonths(int loanMonths) {
        this.loanMonths = loanMonths;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public BigDecimal getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(BigDecimal expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public Long getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Long expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public int getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(int investStatus) {
        this.investStatus = investStatus;
    }

    public int getPlanCanQuit() {
        return planCanQuit;
    }

    public void setPlanCanQuit(int planCanQuit) {
        this.planCanQuit = planCanQuit;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public BigDecimal getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(BigDecimal bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public Long getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Long interestTime) {
        this.interestTime = interestTime;
    }

    public Integer getHoldDays() {
        return holdDays;
    }

    public void setHoldDays(Integer holdDays) {
        this.holdDays = holdDays;
    }

    public String getNovicePlan() {
        return novicePlan;
    }

    public void setNovicePlan(String novicePlan) {
        this.novicePlan = novicePlan;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public BigDecimal getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(BigDecimal voteAmount) {
        this.voteAmount = voteAmount;
    }
}
