package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;

/** 
 * @Description: 定向标信息-随时退出标信息
 * @author: junda.feng
 */
public class DirectionalBid {
	
	private int bidId;//标id
	
	private BigDecimal totalUserAssets; //用户资产总额

	private BigDecimal userInvestAmount; //用户在投金额

	private int targetUser;//是否指定用户可投的标

	private BigDecimal userAccumulatedIncome;//用户累计收益

	private int anytimeQuit;//随时退出标信息 1：是 ； 0：否

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public BigDecimal getTotalUserAssets() {
		return totalUserAssets;
	}

	public void setTotalUserAssets(BigDecimal totalUserAssets) {
		this.totalUserAssets = totalUserAssets;
	}

	public BigDecimal getUserInvestAmount() {
		return userInvestAmount;
	}

	public void setUserInvestAmount(BigDecimal userInvestAmount) {
		this.userInvestAmount = userInvestAmount;
	}

	public int getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(int targetUser) {
		this.targetUser = targetUser;
	}

	public BigDecimal getUserAccumulatedIncome() {
		return userAccumulatedIncome;
	}

	public void setUserAccumulatedIncome(BigDecimal userAccumulatedIncome) {
		this.userAccumulatedIncome = userAccumulatedIncome;
	}

	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
	}
}
