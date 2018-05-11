package com.fenlibao.p2p.model.vo.fiancing;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 投资信息VO
 * Created by laubrence on 2016/3/26.
 */
public class InvestInfoVO implements Serializable {
    /*标Id*/
    private int bidId;
    /*标名称*/
    private String bidTitle;
    /*债权id*/
    private int creditId;
    /*购买时间*/
    private long purchaseTime;
    /*到期时间*/
    private Long expireDate;
    /*投资金额*/
    private BigDecimal investAmount;
    /*年化收益率*/
    private double yearYield;
    /*借款周期（单位：月）*/
    private int loanMonths;
    /*借款周期（单位：天）*/
    private int loanDays;
    /*预期收益*/
    private BigDecimal expectedProfit;
    /*加息预期收益*/
    private BigDecimal rasiedExpectedProfit;
    /*投资状态(1:已投资2:收益中;3:转让中 4:已转出 5:已逾期 6: 已回款)*/
    private int investStatus;
    private int isNoviceBid;//否是新手标 (1:是新手标;0:普通开店宝标)
    private String totalRepaymentAmount; //总还款金额
    private boolean creditFlag;//债权还是项目
    private boolean jxFlag;//是否已加息
    private int isDepository;//是否是存管标 1：普通标  2：存管标

    private BigDecimal voteAmount;

    /*计划1、标0*/
    int itemType;
    /*加息标加息利率*/
    BigDecimal bidInterestRise;
    /*随时退出标：1是、0否*/
    int anytimeQuit;

    /*计划Id*/
    int planId;
    /*计划名称*/
    String planTitle;

    int newPlan;

    String bidStatus;
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(BigDecimal bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public boolean isJxFlag() {
        return jxFlag;
    }

    public void setJxFlag(boolean jxFlag) {
        this.jxFlag = jxFlag;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getBidTitle() {
        return bidTitle;
    }

    public void setBidTitle(String bidTitle) {
        this.bidTitle = bidTitle;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
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

    public int getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(int investStatus) {
        this.investStatus = investStatus;
    }

    public int getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(int isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public String getTotalRepaymentAmount() {
        return totalRepaymentAmount;
    }

    public void setTotalRepaymentAmount(String totalRepaymentAmount) {
        this.totalRepaymentAmount = totalRepaymentAmount;
    }

    public boolean isCreditFlag() {
        return creditFlag;
    }

    public void setCreditFlag(boolean creditFlag) {
        this.creditFlag = creditFlag;
    }

    public int getIsDepository() {
        return isDepository;
    }

    public void setIsDepository(int isDepository) {
        this.isDepository = isDepository;
    }



    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public BigDecimal getRasiedExpectedProfit() {
        return rasiedExpectedProfit;
    }

    public void setRasiedExpectedProfit(BigDecimal rasiedExpectedProfit) {
        this.rasiedExpectedProfit = rasiedExpectedProfit;
    }

    public int getNewPlan() {
        return newPlan;
    }

    public void setNewPlan(int newPlan) {
        this.newPlan = newPlan;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public BigDecimal getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(BigDecimal voteAmount) {
        this.voteAmount = voteAmount;
    }
}
