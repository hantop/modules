package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
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



    private Date investTime;  /*投资时间*/

    private int loanDate;//借款周期

    private String loanType;//借款周期类型（按天/按月）

    private String repaymentMode;

    private double bidInterest;//加息利率

    private double interestRise;//加息券利率


    private String remark;

    private Date purchaseTime;

    private Date expireTime;//到期时间

    private Date interestTime;//计息时间

    private String loanAmount;//计划总额

    private String voteAmount;//剩余金额

    private Date bidFullTime;//满标时间

    private Date bidReviewedTime;

    private String[] bidLable;

    private String assetsType;//资产类型

    private int totalInvestPers;//投资人数

    private int bidNum;//投标次数

    private String planType;//计划类型 1月月升计划 2省心计划

    private double lowRate;//最低年化利率

    private double highRate;//最高年化利率

    private String comment;//产品说明

    private double bonusRate;//月增幅利率

    private long fullTime;

    private int holdDays;//持有天数

    private double presentRate;//当前月利率

    private int anytimeQuit;//是否是随时退出

    private int ownsStatus;//持有状态

    private Date exitTime;//退出时间

    private int isDepository;//是否是存管

    private String bidLabel;//标的标签说明

    private BigDecimal userTotalAssets;//用户资产总额

    private String userInvestAmount;

    private Integer directionalBid;//定向标关联id，用于判断

    private String userInvestingAmount;//用户在投金额

    private String userAccumulatedIncome;//用户累计收益

    private int targetUser;//白名单用户

    private Date applyTime;

    private String accumulatedIncome;


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

    public double getBidInterest() {
        return bidInterest;
    }

    public void setBidInterest(double bidInterest) {
        this.bidInterest = bidInterest;
    }

    public double getInterestRise() {
        return interestRise;
    }

    public void setInterestRise(double interestRise) {
        this.interestRise = interestRise;
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

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(String voteAmount) {
        this.voteAmount = voteAmount;
    }

    public Date getBidFullTime() {
        return bidFullTime;
    }

    public void setBidFullTime(Date bidFullTime) {
        this.bidFullTime = bidFullTime;
    }

    public Date getBidReviewedTime() {
        return bidReviewedTime;
    }

    public void setBidReviewedTime(Date bidReviewedTime) {
        this.bidReviewedTime = bidReviewedTime;
    }

    public String[] getBidLable() {
        return bidLable;
    }

    public void setBidLable(String[] bidLable) {
        this.bidLable = bidLable;
    }

    public String getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    public int getTotalInvestPers() {
        return totalInvestPers;
    }

    public void setTotalInvestPers(int totalInvestPers) {
        this.totalInvestPers = totalInvestPers;
    }

    public int getBidNum() {
        return bidNum;
    }

    public void setBidNum(int bidNum) {
        this.bidNum = bidNum;
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

    public long getFullTime() {
        return fullTime;
    }

    public void setFullTime(long fullTime) {
        this.fullTime = fullTime;
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

    public int getHoldDays() {
        return holdDays;
    }

    public void setHoldDays(int holdDays) {
        this.holdDays = holdDays;
    }

    public double getPresentRate() {
        return presentRate;
    }

    public void setPresentRate(double presentRate) {
        this.presentRate = presentRate;
    }

    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public int getOwnsStatus() {
        return ownsStatus;
    }

    public void setOwnsStatus(int ownsStatus) {
        this.ownsStatus = ownsStatus;
    }

    public int getIsDepository() {
        return isDepository;
    }

    public void setIsDepository(int isDepository) {
        this.isDepository = isDepository;
    }

    public String getBidLabel() {
        return bidLabel;
    }

    public void setBidLabel(String bidLabel) {
        this.bidLabel = bidLabel;
    }

    public BigDecimal getUserTotalAssets() {
        return userTotalAssets;
    }

    public void setUserTotalAssets(BigDecimal userTotalAssets) {
        this.userTotalAssets = userTotalAssets;
    }

    public String getUserInvestAmount() {
        return userInvestAmount;
    }

    public void setUserInvestAmount(String userInvestAmount) {
        this.userInvestAmount = userInvestAmount;
    }

    public Integer getDirectionalBid() {
        return directionalBid;
    }

    public void setDirectionalBid(Integer directionalBid) {
        this.directionalBid = directionalBid;
    }

    public int getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(int targetUser) {
        this.targetUser = targetUser;
    }

    public String getUserAccumulatedIncome() {
        return userAccumulatedIncome;
    }

    public void setUserAccumulatedIncome(String userAccumulatedIncome) {
        this.userAccumulatedIncome = userAccumulatedIncome;
    }

    public String getUserInvestingAmount() {
        return userInvestingAmount;
    }

    public void setUserInvestingAmount(String userInvestingAmount) {
        this.userInvestingAmount = userInvestingAmount;
    }

    public String getAccumulatedIncome() {
        return accumulatedIncome;
    }

    public void setAccumulatedIncome(String accumulatedIncome) {
        this.accumulatedIncome = accumulatedIncome;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
}
