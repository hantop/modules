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

    Date investDate;//投资日期

    String isTransfer; //债权是否正在转让
	
    BigDecimal expectedProfit; //预期收益

    BigDecimal arrivalProfit; //已获收益

    int transferOutId;//债权转出记录id

    Date expireDate; //到期时间
    

    //2016-06-28 junda.feng
    private String repaymentMode;//还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清) 
    private Date applyTime;//债权转让申请时间
    
    private Date successTime;//转让成功日
    
    private Date actualRepaymentDate;//到期回款日
    
	private String interestPaymentType;// 付息方式,ZRY:自然月;GDR:固定日;
	
//  Date nextRepaymentDate;//下次还款日期    2016-06-29 junda.feng

	private BigDecimal originalAmount;//原始的投标价格

	double collectInterest;//待收本息

	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getRepaymentMode() {
		return repaymentMode;
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

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public String getInterestPaymentType() {
		return interestPaymentType;
	}

	public void setInterestPaymentType(String interestPaymentType) {
		this.interestPaymentType = interestPaymentType;
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

	public double getCollectInterest() {
		return collectInterest;
	}

	public void setCollectInterest(double collectInterest) {
		this.collectInterest = collectInterest;
	}
}
