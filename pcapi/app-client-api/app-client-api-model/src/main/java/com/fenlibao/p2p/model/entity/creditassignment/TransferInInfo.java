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

import com.fenlibao.p2p.model.entity.bid.BidExtendInfo;

import java.math.BigDecimal;
import java.util.Date;

/** 
 * @ClassName: TransferInInfo
 * @author: laubrence
 * @date: 2015-10-23 下午1:43:49  
 */
public class TransferInInfo extends BidExtendInfo{

    int creditId; //债权id
	
    String creditCode; //债权编码

    Date purchaseTime; //投资时间戳

    BigDecimal investAmount; //投资金额

    int surplusDays;//剩余天数

    Date investDate;//投资日期（原始）

    String isTransfer; //债权是否正在转让
	
    BigDecimal expectedProfit; //预期收益

    BigDecimal arrivalProfit; //已获收益

    int transferOutId;//债权转出记录id

    Date expireDate; //到期时间

    //junda.feng 20160721
    private Integer zrId; //债权转让订单id
    private Date holdTime; //债权产生时间
    private Integer periodDates;//项目期限
    private Date applyTime; //债权转让申请时间
    private String transferAmount;//转让价格
    private Date successTime; //债权转让成功时间
    private Date actualRepaymentDate; //债权实际回款时间
    
    //junda.feng 20160802
    private BigDecimal creditAmount;//债权金额
    private BigDecimal interest;//预期本息

	int anytimeQuit;//随时退出标：1是、0否

	//wxd 20161013
	private boolean jxFlag; //是否已加息

	private double bidInterestRise ;


	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
	}

	public boolean isJxFlag() {
		return jxFlag;
	}

	public void setJxFlag(boolean jxFlag) {
		this.jxFlag = jxFlag;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public Integer getPeriodDates() {
		return periodDates;
	}

	public void setPeriodDates(Integer periodDates) {
		this.periodDates = periodDates;
	}

	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public Date getActualRepaymentDate() {
		return actualRepaymentDate;
	}

	public void setActualRepaymentDate(Date actualRepaymentDate) {
		this.actualRepaymentDate = actualRepaymentDate;
	}

	public Integer getZrId() {
		return zrId;
	}

	public void setZrId(Integer zrId) {
		this.zrId = zrId;
	}

	public Date getHoldTime() {
		return holdTime;
	}

	public void setHoldTime(Date holdTime) {
		this.holdTime = holdTime;
	}

	@Override
    public Date getExpireDate() {
        return expireDate;
    }

    @Override
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public BigDecimal getArrivalProfit() {
		return arrivalProfit;
	}

	public void setArrivalProfit(BigDecimal arrivalProfit) {
		this.arrivalProfit = arrivalProfit;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public int getCreditId() {
		return creditId;
	}

	public void setCreditId(int creditId) {
		this.creditId = creditId;
	}

	public BigDecimal getExpectedProfit() {
		return expectedProfit;
	}

	public void setExpectedProfit(BigDecimal expectedProfit) {
		this.expectedProfit = expectedProfit;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public Date getInvestDate() {
		return investDate;
	}

	public void setInvestDate(Date investDate) {
		this.investDate = investDate;
	}

	public String getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(String isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public int getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(int surplusDays) {
		this.surplusDays = surplusDays;
	}

	public int getTransferOutId() {
		return transferOutId;
	}

	public void setTransferOutId(int transferOutId) {
		this.transferOutId = transferOutId;
	}

	@Override
	public double getBidInterestRise() {
		return bidInterestRise;
	}

	@Override
	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
	}
}
