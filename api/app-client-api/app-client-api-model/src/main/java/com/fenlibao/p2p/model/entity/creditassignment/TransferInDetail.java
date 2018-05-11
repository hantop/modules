/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInDetail.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 下午1:44:46 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.entity.creditassignment;

import java.math.BigDecimal;
import java.util.Date;

/** 
 * @ClassName: TransferInDetail 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-23 下午1:44:46  
 */
public class TransferInDetail {
	
	public int creditId; //债权id
	
	public String creditTitle; //债权标题
	
	public int bidId; //标id
	
	public Date purchaseTime; //投资时间
	
	public String creditStatus; //还款状态
	
	public BigDecimal investAmount; //投资金额
	
	public double annualYield; //年化收益率
	
	public int remainDays; //剩余天数
	
	public BigDecimal exceptEarnings; //预计总收益
	
	public String isTransfer; //债权是否正在转让
	
	public Date investDay; //投资日期时间戳
	
	public Date interestDay; //计息日期时间戳
	
	public Date expireDay; //到期日期时间戳
	
	public String remark; //项目描述
	
	/**
	 * @return the creditTitle
	 */
	public String getCreditTitle() {
		return creditTitle;
	}

	/**
	 * @param creditTitle the creditTitle to set
	 */
	public void setCreditTitle(String creditTitle) {
		this.creditTitle = creditTitle;
	}

	/**
	 * @return the investAmount
	 */
	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	/**
	 * @param investAmount the investAmount to set
	 */
	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	/**
	 * @return the annualYield
	 */
	public double getAnnualYield() {
		return annualYield;
	}

	/**
	 * @param annualYield the annualYield to set
	 */
	public void setAnnualYield(double annualYield) {
		this.annualYield = annualYield;
	}

	/**
	 * @return the remainDays
	 */
	public int getRemainDays() {
		return remainDays;
	}

	/**
	 * @param remainDays the remainDays to set
	 */
	public void setRemainDays(int remainDays) {
		this.remainDays = remainDays;
	}

	/**
	 * @return the creditId
	 */
	public int getCreditId() {
		return creditId;
	}

	/**
	 * @param creditId the creditId to set
	 */
	public void setCreditId(int creditId) {
		this.creditId = creditId;
	}

	/**
	 * @return the bidId
	 */
	public int getBidId() {
		return bidId;
	}

	/**
	 * @param bidId the bidId to set
	 */
	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	/**
	 * @return the exceptEarnings
	 */
	public BigDecimal getExceptEarnings() {
		return exceptEarnings;
	}

	/**
	 * @param exceptEarnings the exceptEarnings to set
	 */
	public void setExceptEarnings(BigDecimal exceptEarnings) {
		this.exceptEarnings = exceptEarnings;
	}

	/**
	 * @return the isTransfer
	 */
	public String getIsTransfer() {
		return isTransfer;
	}

	/**
	 * @param isTransfer the isTransfer to set
	 */
	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	/**
	 * @return the purchaseTime
	 */
	public Date getPurchaseTime() {
		return purchaseTime;
	}

	/**
	 * @param purchaseTime the purchaseTime to set
	 */
	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	/**
	 * @return the investDay
	 */
	public Date getInvestDay() {
		return investDay;
	}

	/**
	 * @param investDay the investDay to set
	 */
	public void setInvestDay(Date investDay) {
		this.investDay = investDay;
	}

	/**
	 * @return the interestDay
	 */
	public Date getInterestDay() {
		return interestDay;
	}

	/**
	 * @param interestDay the interestDay to set
	 */
	public void setInterestDay(Date interestDay) {
		this.interestDay = interestDay;
	}

	/**
	 * @return the expireDay
	 */
	public Date getExpireDay() {
		return expireDay;
	}

	/**
	 * @param expireDay the expireDay to set
	 */
	public void setExpireDay(Date expireDay) {
		this.expireDay = expireDay;
	}

	/**
	 * @return the creditStatus
	 */
	public String getCreditStatus() {
		return creditStatus;
	}

	/**
	 * @param creditStatus the creditStatus to set
	 */
	public void setCreditStatus(String creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
