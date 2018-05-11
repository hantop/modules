package com.fenlibao.p2p.model.entity.finacing;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/4.
 */
public class PlanFinacing {

    private int  id;

    private int itemType;

    private int planRecordId;

    private String bidTitle;

    private String isNoviceBid;

    private String investAmount;//投资金额

    private double yearYield;//年化收益率

    private String investStatus;

    private String expectedProfit;//预期收益

    private Date applyTime;//债权转让申请时间

    private Date investTime;  /*投资时间*/

    private int loanDate;//借款周期

    private String loanType;//借款周期类型（按天/按月）

    private String repaymentMode;

    private double interest;//加息利率

    private String remark;

    private Date purchaseTime;

    private Date expireTime;//到期时间

    private Date interestTime;//计息时间

    private Date nextRepayTime;//下一回款日

    private Date successTime;//转让成功日

    private Date actualRepaymentDate;//到期回款日

    private String planType;//计划类型 1月月升计划 2省心计划

    private double lowRate;//最低年化利率

    private double highRate;//最高年化利率

    private String comment;//产品说明

    private double bonusRate;//月增幅利率

    private int anytimeQuit;//是否是随时退出

    private int ownsStatus;//持有状态

    private Date exitTime;//退出时间

    private int currentMonth;//当前月份

    private String isCG;
    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getBidTitle() {
        return bidTitle;
    }

    public void setBidTitle(String bidTitle) {
        this.bidTitle = bidTitle;
    }

    public String getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(String expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(String investStatus) {
        this.investStatus = investStatus;
    }

    public String getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(String isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getPlanRecordId() {
        return planRecordId;
    }

    public void setPlanRecordId(int planRecordId) {
        this.planRecordId = planRecordId;
    }

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public int getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(int loanDate) {
        this.loanDate = loanDate;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }

    public Date getNextRepayTime() {
        return nextRepayTime;
    }

    public void setNextRepayTime(Date nextRepayTime) {
        this.nextRepayTime = nextRepayTime;
    }

    public double getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(double bonusRate) {
        this.bonusRate = bonusRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getHighRate() {
        return highRate;
    }

    public void setHighRate(double highRate) {
        this.highRate = highRate;
    }

    public double getLowRate() {
        return lowRate;
    }

    public void setLowRate(double lowRate) {
        this.lowRate = lowRate;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public int getOwnsStatus() {
        return ownsStatus;
    }

    public void setOwnsStatus(int ownsStatus) {
        this.ownsStatus = ownsStatus;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public Date getActualRepaymentDate() {
        return actualRepaymentDate;
    }

    public void setActualRepaymentDate(Date actualRepaymentDate) {
        this.actualRepaymentDate = actualRepaymentDate;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public String getIsCG() {
        return isCG;
    }

    public void setIsCG(String isCG) {
        this.isCG = isCG;
    }
}
