package com.fenlibao.p2p.model.vo.creditassignment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 已购买的债权
 *
 * @author: chenzhixuan
 * @date: 2016-07-29
 */
public class TransferInBuyedInfoVO {

	int creditId; //债权id

	String creditTitle; //债权标题

	Date purchaseTime; //投资时间戳

	int creditStatus; //债权状态（0：已回款；1:收益中）

	String investAmount; //投资金额

	double yearYield; //年化收益率

	int surplusDays;//剩余天数

	BigDecimal expectedProfit; //预期收益

	Date expireDate; //到期时间

	Boolean jxFlag; // 是否已加息

	public Boolean getJxFlag() {
		return jxFlag;
	}

	public void setJxFlag(Boolean jxFlag) {
		this.jxFlag = jxFlag;
	}

	public int getCreditId() {
		return creditId;
	}

	public void setCreditId(int creditId) {
		this.creditId = creditId;
	}

	public String getCreditTitle() {
		return creditTitle;
	}

	public void setCreditTitle(String creditTitle) {
		this.creditTitle = creditTitle;
	}

	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public int getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(int creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(String investAmount) {
		this.investAmount = investAmount;
	}

	public double getYearYield() {
		return yearYield;
	}

	public void setYearYield(double yearYield) {
		this.yearYield = yearYield;
	}

	public int getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(int surplusDays) {
		this.surplusDays = surplusDays;
	}

	public BigDecimal getExpectedProfit() {
		return expectedProfit;
	}

	public void setExpectedProfit(BigDecimal expectedProfit) {
		this.expectedProfit = expectedProfit;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}