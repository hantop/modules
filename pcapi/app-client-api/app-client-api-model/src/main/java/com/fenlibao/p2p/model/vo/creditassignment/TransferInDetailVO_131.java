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
import java.util.List;

import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

/** 
 * @ClassName: TransferInDetailVO 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-23 上午11:20:59  
 */
public class TransferInDetailVO_131 {
	
	public String creditTitle; //债权标题
	
	public long purchaseTime; //投资时间
	
	public int creditStatus; //债权状态（0：已回款；1:收益中;2:转让中）
	
	public BigDecimal investAmount; //投资金额
	
	public double annualYield; //年化收益率
	
	public int remainDays; //剩余天数
	
	public BigDecimal exceptEarnings; //预计总收益
	
	public BigDecimal getExceptEarnings() {
		return exceptEarnings;
	}

	public void setExceptEarnings(BigDecimal exceptEarnings) {
		this.exceptEarnings = exceptEarnings;
	}

	public long investDay; //投资日期时间戳
	
	public long interestDay; //计息日期时间戳
	
	public long expireDay; //到期日期时间戳
	
	public String serviceArgeementUrl; //服务协议
	
	private String borrowerUrl;//借款人信息url
	
	private String[] lawFiles;//法律文书页面链接
	
	private String lawFileUrl;//法律文书url
	
	private String remark;//项目描述
	
	private List<BidExtendGroupVO> groupInfoList;

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

	public String getBorrowerUrl() {
		return borrowerUrl;
	}

	public void setBorrowerUrl(String borrowerUrl) {
		this.borrowerUrl = borrowerUrl;
	}

	public String[] getLawFiles() {
		return lawFiles;
	}

	public void setLawFiles(String[] lawFiles) {
		this.lawFiles = lawFiles;
	}

	public String getLawFileUrl() {
		return lawFileUrl;
	}

	public void setLawFileUrl(String lawFileUrl) {
		this.lawFileUrl = lawFileUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<BidExtendGroupVO> getGroupInfoList() {
		return groupInfoList;
	}

	public void setGroupInfoList(List<BidExtendGroupVO> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}
}
