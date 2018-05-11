package com.fenlibao.p2p.model.vo.bidinfo;

import java.io.Serializable;
import java.util.List;

/**
 * 标的基本信息
 */
public class BidInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int bidId;//标ID
	
	private String bidTitle;//标的标题

	private int planId;

	private String planTitle;
	
	private String bidType;//标的类型
	
	private String bidYield;//标的年化利率

	private int bidStatus;//标的状态
	
	private int isNoviceBid;//是否新手标
	
	private int loanDays;//借款周期（单位：天）
	
	private int loanMonth;//借款周期（单位：月）

	private String[] assetTypes;//资产类型（信用认证、实地认证、抵押担保）

	private long timestamp;//发标时间(秒数)
	
	private String loanAmount;//借款金额

	private String surplusAmount;//剩余金额

	//20161008 ad by junda.feng
	private int bidClassify;//标类型 0:普通标 1：新手标 2定向标

	private Long panicBuyingTime;//抢购时间（抢购标）

	private Long timeLeft;//剩余时间

	private Integer countdown;//倒计时配置参数

	private String repaymentMode;//还款方式

	private String userTotalAssets;//用户资产总额

	private String userInvestingAmount;//用户在投金额

	private String userAccumulatedIncome;//用户累计收益

	private int targetUser;//白名单用户

	private int isDepository;//是否是存管标

	private int itemType;//区分标和计划 0:标 1：计划

	private List<String> bidLabel;//标的说明

	private double bidInterestRise;//加息利率

	private int anytimeQuit;//是否是随时退出标

	private String planType;//计划类型

	private String lowRate;//最低年化利率

	private String highRate;//最高年化利率

	private List<String> markArray;//定向标/计划提示语

	private String comment;//广告语

	private int newPlan;//区分3.1和3.2计划（0：3.1计划  1:3.2计划）

	private int planCanQuit;//是否支持推迟 0：否  1：是

	public Long getPanicBuyingTime() {
		return panicBuyingTime;
	}

	public void setPanicBuyingTime(Long panicBuyingTime) {
		this.panicBuyingTime = panicBuyingTime;
	}

	public Long getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(Long timeLeft) {
		this.timeLeft = timeLeft;
	}

	public Integer getCountdown() {
		return countdown;
	}

	public void setCountdown(Integer countdown) {
		this.countdown = countdown;
	}

	public int getBidClassify() {
		return bidClassify;
	}

	public void setBidClassify(int bidClassify) {
		this.bidClassify = bidClassify;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public String getUserTotalAssets() {
		return userTotalAssets;
	}

	public void setUserTotalAssets(String userTotalAssets) {
		this.userTotalAssets = userTotalAssets;
	}

	public String[] getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(String[] assetTypes) {
		this.assetTypes = assetTypes;
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public int getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(int bidStatus) {
		this.bidStatus = bidStatus;
	}

	public String getBidTitle() {
		return bidTitle;
	}

	public void setBidTitle(String bidTitle) {
		this.bidTitle = bidTitle;
	}

	public String getBidType() {
		return bidType;
	}

	public void setBidType(String bidType) {
		this.bidType = bidType;
	}

	public String getBidYield() {
		return bidYield;
	}

	public void setBidYield(String bidYield) {
		this.bidYield = bidYield;
	}

	public int getIsNoviceBid() {
		return isNoviceBid;
	}

	public void setIsNoviceBid(int isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	public int getLoanDays() {
		return loanDays;
	}

	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public int getLoanMonth() {
		return loanMonth;
	}

	public void setLoanMonth(int loanMonth) {
		this.loanMonth = loanMonth;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null){  
            return false;  
        }else {
        	if(this.getClass() == obj.getClass()){
        		BidInfoVO info = (BidInfoVO) obj;
				if(this.getBidId() == info.getBidId()){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
        }
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getSurplusAmount() {
		return surplusAmount;
	}

	public void setSurplusAmount(String surplusAmount) {
		this.surplusAmount = surplusAmount;
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

	public List<String> getBidLabel() {
		return bidLabel;
	}

	public void setBidLabel(List<String> bidLabel) {
		this.bidLabel = bidLabel;
	}

	public double getBidInterestRise() {
		return bidInterestRise;
	}

	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
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

	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
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

	public int getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(int targetUser) {
		this.targetUser = targetUser;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHighRate() {
		return highRate;
	}

	public void setHighRate(String highRate) {
		this.highRate = highRate;
	}

	public String getLowRate() {
		return lowRate;
	}

	public void setLowRate(String lowRate) {
		this.lowRate = lowRate;
	}

	public List<String> getMarkArray() {
		return markArray;
	}

	public void setMarkArray(List<String> markArray) {
		this.markArray = markArray;
	}

	public int getNewPlan() {
		return newPlan;
	}

	public void setNewPlan(int newPlan) {
		this.newPlan = newPlan;
	}

	public int getPlanCanQuit() {
		return planCanQuit;
	}

	public void setPlanCanQuit(int planCanQuit) {
		this.planCanQuit = planCanQuit;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}
}
