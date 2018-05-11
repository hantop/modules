/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInList.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.entity.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 下午1:43:49 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.entity.creditassignment;

import java.math.BigDecimal;
import java.util.Date;

/** 
 * @ClassName: TransferInList 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-23 下午1:43:49  
 */
public class TransferInList {
	
	public int creditId; //债权id
	
	public String creditTitle; //债权标题
	
	public Date purchaseTime; //投资时间戳
	
	public String creditStatus; //债权状态（WH,YH）
	
	public BigDecimal investAmount; //投资金额
	
	public double annualYield; //年化收益率
	
	public int remainDays;//剩余天数
	
	public BigDecimal exceptEarnings; //预计总收益
	
	public String isTransfer; //债权是否正在转让

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

}
