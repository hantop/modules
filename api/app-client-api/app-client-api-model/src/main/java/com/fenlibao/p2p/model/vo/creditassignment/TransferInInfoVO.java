/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInListVO.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 上午11:20:39 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.vo.creditassignment;

import java.math.BigDecimal;

/** 
 * @ClassName: TransferInListVO 
 * @author: laubrence
 * @date: 2015-10-23 上午11:20:39  
 */
public class TransferInInfoVO {
	
    int creditId; //债权id
	
    String creditTitle; //债权标题
	
    long purchaseTime; //投资时间戳
	
    int creditStatus; //债权状态（0：已回款；1:收益中）
	
    String investAmount; //投资金额

    double yearYield; //年化收益率
	
    int surplusDays;//剩余天数

    BigDecimal expectedProfit; //预期收益
    
    Long nextRepaymentDate;//下次还款日期    2016-06-29 junda.feng
    Long applyTime;//债权转让申请时间
    Long successTime;//转让成功日
    Long actualRepaymentDate;//到期回款日

	String repaymentMode;//还款方式

    
	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public Long getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Long successTime) {
		this.successTime = successTime;
	}

	public Long getActualRepaymentDate() {
		return actualRepaymentDate;
	}

	public void setActualRepaymentDate(Long actualRepaymentDate) {
		this.actualRepaymentDate = actualRepaymentDate;
	}

	public Long getNextRepaymentDate() {
		return nextRepaymentDate;
	}

	public void setNextRepaymentDate(Long nextRepaymentDate) {
		this.nextRepaymentDate = nextRepaymentDate;
	}

	public int getCreditId() {
		return creditId;
	}

	public void setCreditId(int creditId) {
		this.creditId = creditId;
	}

	public int getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(int creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getCreditTitle() {
		return creditTitle;
	}

	public void setCreditTitle(String creditTitle) {
		this.creditTitle = creditTitle;
	}

	public BigDecimal getExpectedProfit() {
		return expectedProfit;
	}

	public void setExpectedProfit(BigDecimal expectedProfit) {
		this.expectedProfit = expectedProfit;
	}



	public String getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(String investAmount) {
		this.investAmount = investAmount;
	}

	public long getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(long purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public int getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(int surplusDays) {
		this.surplusDays = surplusDays;
	}

	public double getYearYield() {
		return yearYield;
	}

	public void setYearYield(double yearYield) {
		this.yearYield = yearYield;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}
}
