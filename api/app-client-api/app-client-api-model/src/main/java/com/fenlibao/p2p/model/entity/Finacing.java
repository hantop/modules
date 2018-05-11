package com.fenlibao.p2p.model.entity;

import java.util.Date;


/**
 *债权 t6251
 */
public class Finacing {

	private int id;//债权ID
	
	private String name;//债权名称
	
	private double money;//投资金额
	
	private int period;//债权还款期数
	
	private double rate;//年化利率
	
	private int month;//借款周期
	
	private Date createTime;//债权创建时间
	
	private String bidStatus;//标状态
	
	private String isTransfer;//是否正在转让

	private Date buyTimestamp; //申购时间戳
	
	private Date interestTimestamp; //计息时间戳
	
	private Date endTimestamp; //到期时间戳
	
	private double zqEarning; //已获收益
	
	private String paymentType;//还款方式
	
	private double originalMoney;//原始债权金额
	
	private int bidId;//标ID
	
	private String isNoviceBid;//是否是新手标 (S:是新手标;F:普通开店宝标)
	
	private int loanDays;//借款周期（单位：天）
	
	private String remark; //借款描述

	private String repaymentMethod; //还款方式

	private String bidType; //标的类型

	private int anytimeQuit; //随时退出标：1是、0否

	private Date nextRepaymentTime; //下一期还款时间戳

	private Date beginTimestamp; //起息时间

	public Date getBeginTimestamp() {
		return beginTimestamp;
	}

	public void setBeginTimestamp(Date beginTimestamp) {
		this.beginTimestamp = beginTimestamp;
	}

	public Date getNextRepaymentTime() {
		return nextRepaymentTime;
	}

	public void setNextRepaymentTime(Date nextRepaymentTime) {
		this.nextRepaymentTime = nextRepaymentTime;
	}

	public String getBidType() {
		return bidType;
	}

	public void setBidType(String bidType) {
		this.bidType = bidType;
	}

	public String getRepaymentMethod() {
		return repaymentMethod;
	}

	public void setRepaymentMethod(String repaymentMethod) {
		this.repaymentMethod = repaymentMethod;
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public double getOriginalMoney() {
		return originalMoney;
	}

	public void setOriginalMoney(double originalMoney) {
		this.originalMoney = originalMoney;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}

	public String getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Date getBuyTimestamp() {
		return buyTimestamp;
	}

	public void setBuyTimestamp(Date buyTimestamp) {
		this.buyTimestamp = buyTimestamp;
	}

	public Date getInterestTimestamp() {
		return interestTimestamp;
	}

	public void setInterestTimestamp(Date interestTimestamp) {
		this.interestTimestamp = interestTimestamp;
	}

	public Date getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public double getZqEarning() {
		return zqEarning;
	}

	public void setZqEarning(double zqEarning) {
		this.zqEarning = zqEarning;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getIsNoviceBid() {
		return isNoviceBid;
	}

	public void setIsNoviceBid(String isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	/**
	 * @return the loanDays
	 */
	public int getLoanDays() {
		return loanDays;
	}

	/**
	 * @param loanDays the loanDays to set
	 */
	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
	}
}
