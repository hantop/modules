/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInDetailVO.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 上午11:20:59 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.vo.creditassignment;

import java.math.BigDecimal;

/** 
 * @ClassName: TransferInDetailVO 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-23 上午11:20:59  
 */
public class TransferInDetailVO {
	
	public String creditTitle; //债权标题
	
	public long purchaseTime; //投资时间
	
	public int creditStatus; //债权状态（0：已回款；1:收益中;2:转让中）
	
	public BigDecimal investAmount; //投资金额
	
	public double annualYield; //年化收益率
	
	public int remainDays; //剩余天数
	
	public BigDecimal exceptEarnings; //预计总收益
	
	public long investDay; //投资日期时间戳
	
	public long interestDay; //计息日期时间戳
	
	public long expireDay; //到期日期时间戳
	
	public String shopDetailUrl; //店铺明细
	
	public String productDetailUrl; //产品明细
	
	public String serviceArgeementUrl; //服务协议
	
	public String loanContractUrl; //借款合同

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
	 * @return the creditStatus
	 */
	public int getCreditStatus() {
		return creditStatus;
	}

	/**
	 * @param creditStatus the creditStatus to set
	 */
	public void setCreditStatus(int creditStatus) {
		this.creditStatus = creditStatus;
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
	 * @return the investDay
	 */
	public long getInvestDay() {
		return investDay;
	}

	/**
	 * @param investDay the investDay to set
	 */
	public void setInvestDay(long investDay) {
		this.investDay = investDay;
	}

	/**
	 * @return the interestDay
	 */
	public long getInterestDay() {
		return interestDay;
	}

	/**
	 * @param interestDay the interestDay to set
	 */
	public void setInterestDay(long interestDay) {
		this.interestDay = interestDay;
	}

	/**
	 * @return the expireDay
	 */
	public long getExpireDay() {
		return expireDay;
	}

	/**
	 * @param expireDay the expireDay to set
	 */
	public void setExpireDay(long expireDay) {
		this.expireDay = expireDay;
	}

	/**
	 * @return the shopDetailUrl
	 */
	public String getShopDetailUrl() {
		return shopDetailUrl;
	}

	/**
	 * @param shopDetailUrl the shopDetailUrl to set
	 */
	public void setShopDetailUrl(String shopDetailUrl) {
		this.shopDetailUrl = shopDetailUrl;
	}

	/**
	 * @return the productDetailUrl
	 */
	public String getProductDetailUrl() {
		return productDetailUrl;
	}

	/**
	 * @param productDetailUrl the productDetailUrl to set
	 */
	public void setProductDetailUrl(String productDetailUrl) {
		this.productDetailUrl = productDetailUrl;
	}

	/**
	 * @return the serviceArgeementUrl
	 */
	public String getServiceArgeementUrl() {
		return serviceArgeementUrl;
	}

	/**
	 * @param serviceArgeementUrl the serviceArgeementUrl to set
	 */
	public void setServiceArgeementUrl(String serviceArgeementUrl) {
		this.serviceArgeementUrl = serviceArgeementUrl;
	}

	/**
	 * @return the loanContractUrl
	 */
	public String getLoanContractUrl() {
		return loanContractUrl;
	}

	/**
	 * @param loanContractUrl the loanContractUrl to set
	 */
	public void setLoanContractUrl(String loanContractUrl) {
		this.loanContractUrl = loanContractUrl;
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
	public long getPurchaseTime() {
		return purchaseTime;
	}

	/**
	 * @param purchaseTime the purchaseTime to set
	 */
	public void setPurchaseTime(long purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
}
