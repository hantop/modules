package com.fenlibao.p2p.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 标的 信息（t6230）
 */
public class ShopTreasureInfo {

	private int id;//标ID

	private int userId;//用户ID（借款人）

	private String name;//借款标题

	private BigDecimal loanAmount;//借款金额

	private double rate;//年化利率

	private String repaymentMode;//还款方式

	private BigDecimal voteAmount;//可投金额

	private int month;//借款周期（单位：月）

	private Date publishDate;//发布时间

	private Date interestDate;//计息时间

	private Date endDate;//结束时间

	private int fundraisDay;//筹款天数

	private Date fundraisDate;//筹款到期时间

	private String description;//借款描述

	private String status;//标的状态

	private int finacingId;//当前用户当前标的债权ID

	private String isNoviceBid;//是否是新手标

	private int loanDays;//借款周期（单位：天）

	private String assetsType;//资产类型

	private String borrowingDescribe;//借款描述

	private String bidType;//标的类型


	private Date bidReviewedTime;//标的审核通过实践

	private Date bidFullTime; //满标时间

	private int totalInvestPers;//投资人数


	private Date panicBuyingTime;//上架时间（抢购标）

	private Integer directionalBid;//定向标关联id，用于判断

	private BigDecimal userTotalAssets;//用户资产总额

	private String userInvestingAmount;//用户在投金额

	private String userAccumulatedIncome;//用户累计收益

	private int targetUser;//白名单用户

	private int isDepository;//是否是存管标 1普通标  2存管标

	private int itemType;//区分标与计划 0：标  1：计划

	private int anytimeQuit;//是否是随时退出标

	private double bidInterestRise;//加息利率

	private String bidLabel;//标的说明

	private String accumulatedIncome;//用户累计收益

	private String investingAmount;//用户在投金额

	private String planType;//计划类型 1月月升计划 2省心计划

	private double lowRate;//最低年化利率

	private double highRate;//最高年化利率

	private String comment;//产品说明

	private double bonusRate;//月增幅利率

	private long fullTime;

	private String userInvestAmount;

	private String number;

	private String repaymentOrigin;

	private String loanUsage;

	private String guaranteeMeasure;//担保措施

	public Integer getDirectionalBid() {
		return directionalBid;
	}

	public void setDirectionalBid(Integer directionalBid) {
		this.directionalBid = directionalBid;
	}

	public Date getPanicBuyingTime() {
		return panicBuyingTime;
	}

	public void setPanicBuyingTime(Date panicBuyingTime) {
		this.panicBuyingTime = panicBuyingTime;
	}

	public BigDecimal getUserTotalAssets() {
		return userTotalAssets;
	}

	public void setUserTotalAssets(BigDecimal userTotalAssets) {
		this.userTotalAssets = userTotalAssets;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public BigDecimal getVoteAmount() {
		return voteAmount;
	}

	public void setVoteAmount(BigDecimal voteAmount) {
		this.voteAmount = voteAmount;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Date getInterestDate() {
		return interestDate;
	}

	public void setInterestDate(Date interestDate) {
		this.interestDate = interestDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getFundraisDay() {
		return fundraisDay;
	}

	public void setFundraisDay(int fundraisDay) {
		this.fundraisDay = fundraisDay;
	}

	public Date getFundraisDate() {
		return fundraisDate;
	}

	public void setFundraisDate(Date fundraisDate) {
		this.fundraisDate = fundraisDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getFinacingId() {
		return finacingId;
	}

	public void setFinacingId(int finacingId) {
		this.finacingId = finacingId;
	}

	public String getIsNoviceBid() {
		return isNoviceBid;
	}

	public void setIsNoviceBid(String isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	public int getLoanDays() {
		return loanDays;
	}

	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public String getAssetsType() {
		return assetsType;
	}

	public void setAssetsType(String assetsType) {
		this.assetsType = assetsType;
	}

	public String getBorrowingDescribe() {
		return borrowingDescribe;
	}

	public void setBorrowingDescribe(String borrowingDescribe) {
		this.borrowingDescribe = borrowingDescribe;
	}

	public String getBidType() {
		return bidType;
	}

	public void setBidType(String bidType) {
		this.bidType = bidType;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public Date getBidReviewedTime() {
		return bidReviewedTime;
	}

	public void setBidReviewedTime(Date bidReviewedTime) {
		this.bidReviewedTime = bidReviewedTime;
	}

	public Date getBidFullTime() {
		return bidFullTime;
	}

	public void setBidFullTime(Date bidFullTime) {
		this.bidFullTime = bidFullTime;
	}

	public int getTotalInvestPers() {
		return totalInvestPers;
	}

	public void setTotalInvestPers(int totalInvestPers) {
		this.totalInvestPers = totalInvestPers;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}else {
			if(this.getClass() == obj.getClass()){
				ShopTreasureInfo info = (ShopTreasureInfo) obj;
				if(this.getId() == info.getId()){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	}

	public int getIsDepository() {
		return isDepository;
	}

	public void setIsDepository(int isDepository) {
		this.isDepository = isDepository;
	}


	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public String getBidLabel() {
		return bidLabel;
	}

	public void setBidLabel(String bidLabel) {
		this.bidLabel = bidLabel;
	}

	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
	}

	public double getBidInterestRise() {
		return bidInterestRise;
	}

	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
	}

	public int getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(int targetUser) {
		this.targetUser = targetUser;
	}

	public String getUserInvestingAmount() {
		return userInvestingAmount;
	}

	public void setUserInvestingAmount(String userInvestingAmount) {
		this.userInvestingAmount = userInvestingAmount;
	}

	public String getUserAccumulatedIncome() {
		return userAccumulatedIncome;
	}

	public void setUserAccumulatedIncome(String userAccumulatedIncome) {
		this.userAccumulatedIncome = userAccumulatedIncome;
	}

	public String getAccumulatedIncome() {
		return accumulatedIncome;
	}

	public void setAccumulatedIncome(String accumulatedIncome) {
		this.accumulatedIncome = accumulatedIncome;
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

	public String getInvestingAmount() {
		return investingAmount;
	}

	public void setInvestingAmount(String investingAmount) {
		this.investingAmount = investingAmount;
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

	public String getUserInvestAmount() {
		return userInvestAmount;
	}

	public void setUserInvestAmount(String userInvestAmount) {
		this.userInvestAmount = userInvestAmount;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLoanUsage() {
		return loanUsage;
	}

	public void setLoanUsage(String loanUsage) {
		this.loanUsage = loanUsage;
	}

	public String getRepaymentOrigin() {
		return repaymentOrigin;
	}

	public void setRepaymentOrigin(String repaymentOrigin) {
		this.repaymentOrigin = repaymentOrigin;
	}

	public String getGuaranteeMeasure() {
		return guaranteeMeasure;
	}

	public void setGuaranteeMeasure(String guaranteeMeasure) {
		this.guaranteeMeasure = guaranteeMeasure;
	}
}
