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
	private Date panicBuyingTime;//上架时间（抢购标）
	private Integer directionalBid;//定向标关联id，用于判断
	private BigDecimal userTotalAssets;//用户资产总额
	private BigDecimal userInvestAmount;//用户累计投资金额
	private String  bidOrigin;//标的来源：（分利宝：0001	缺钱么：0002）
	private int isCG;//1：普通表 2：存管标
	private double interest;//加息利率
	private int anytimeQuit;//是否是随时退出标
	private String bidLabel;//标的说明
	private int itemType;//区分数组的item类型(0:标  1:计划)
	private String accumulatedIncome;//用户累计收益
	private int targetUser;//指定用户：1指定、0不指定
	private String investingAmount;//用户在投金额
	private String planType;//计划类型 1月月升计划 2省心计划
	private double lowRate;//最低年化利率
	private double highRate;//最高年化利率
	private String comment;//产品说明
	private double bonusRate;//月增幅利率
	private long fullTime;
	private String fundPurpose; //资金用途
	private String repaymentSource;  //还款来源
	private int guaranteeMeasure;  //担保措施



	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
	}

	public String getBidLabel() {
		return bidLabel;
	}

	public void setBidLabel(String bidLabel) {
		this.bidLabel = bidLabel;
	}

	public String getBidOrigin() {
		return bidOrigin;
	}

	public void setBidOrigin(String bidOrigin) {
		this.bidOrigin = bidOrigin;
	}

	public Integer getDirectionalBid() {
		return directionalBid;
	}

	public void setDirectionalBid(Integer directionalBid) {
		this.directionalBid = directionalBid;
	}

	public BigDecimal getUserTotalAssets() {
		return userTotalAssets;
	}

	public void setUserTotalAssets(BigDecimal userTotalAssets) {
		this.userTotalAssets = userTotalAssets;
	}

	public BigDecimal getUserInvestAmount() {
		return userInvestAmount;
	}

	public void setUserInvestAmount(BigDecimal userInvestAmount) {
		this.userInvestAmount = userInvestAmount;
	}

	public Date getPanicBuyingTime() {
		return panicBuyingTime;
	}

	public void setPanicBuyingTime(Date panicBuyingTime) {
		this.panicBuyingTime = panicBuyingTime;
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

	public int getIsCG() {
		return isCG;
	}

	public void setIsCG(int isCG) {
		this.isCG = isCG;
	}


	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public String getAccumulatedIncome() {
		return accumulatedIncome;
	}

	public void setAccumulatedIncome(String accumulatedIncome) {
		this.accumulatedIncome = accumulatedIncome;
	}

	public int getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(int targetUser) {
		this.targetUser = targetUser;
	}

	public String getInvestingAmount() {
		return investingAmount;
	}

	public void setInvestingAmount(String investingAmount) {
		this.investingAmount = investingAmount;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public double getBonusRate() {
		return bonusRate;
	}

	public void setBonusRate(double bonusRate) {
		this.bonusRate = bonusRate;
	}

	public long getFullTime() {
		return fullTime;
	}

	public void setFullTime(long fullTime) {
		this.fullTime = fullTime;
	}

	public String getFundPurpose() {
		return fundPurpose;
	}

	public void setFundPurpose(String fundPurpose) {
		this.fundPurpose = fundPurpose;
	}

	public String getRepaymentSource() {
		return repaymentSource;
	}

	public void setRepaymentSource(String repaymentSource) {
		this.repaymentSource = repaymentSource;
	}

	public int getGuaranteeMeasure() {
		return guaranteeMeasure;
	}

	public void setGuaranteeMeasure(int guaranteeMeasure) {
		this.guaranteeMeasure = guaranteeMeasure;
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
}
