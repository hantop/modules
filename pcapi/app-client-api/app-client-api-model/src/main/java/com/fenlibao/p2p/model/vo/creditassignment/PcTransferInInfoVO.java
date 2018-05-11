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

import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;

/** 
 * pc端的债权转让列表
 * @author: junda.feng
 */
public class PcTransferInInfoVO {
	
    private Integer creditId; //债权id
    private String title; //债权标题
    private Long purchaseTime; //投资时间戳
    private Integer creditStatus; //债权状态(1:已投资2:收益中;3:转让中 4:已转出 5:已逾期 6: 已回款)
    private BigDecimal investAmount; //投资金额
    private Double yearYield; //年化收益率
    private Integer surplusDays;//剩余天数
    private BigDecimal expectedProfit; //预期收益
    private Long nextRepaymentDate;//到期时间
    private Long applyTime;//申请时间
    private Long successTime;//转让成功时间
    private Long actualRepaymentDate;//实际回款时间

    public PcTransferInInfoVO(TransferInInfo transferInInfo) {
    	this.creditId=transferInInfo.getCreditId();
    	this.title=transferInInfo.getZrId()==0?transferInInfo.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX+transferInInfo.getCreditCode();
    	this.purchaseTime=transferInInfo.getHoldTime()!=null?transferInInfo.getHoldTime().getTime()/1000:null;
    	 //标的状态
        if (Status.TBZ.name().equals(transferInInfo.getBidStatus()) || Status.DFK.name().equals(transferInInfo.getBidStatus())) {
        	this.creditStatus=1;
        }
        if (Status.HKZ.name().equals(transferInInfo.getBidStatus())) {
        	this.creditStatus=2;
        }
        if ("S".equals(transferInInfo.getIsTransfer())) {
        	this.creditStatus=3;
        }
        if (Status.YJQ.name().equals(transferInInfo.getBidStatus())) {
        	this.creditStatus=6;
        }
        if (!"F".equals(transferInInfo.getIsYq())) {
        	this.creditStatus=5;
        }
        if (transferInInfo.getTransferOutId() > 0) {
        	this.creditStatus=4;
        	this.expectedProfit=transferInInfo.getArrivalProfit();
        }else{
        	this.expectedProfit=transferInInfo.getExpectedProfit();
        }
        this.investAmount=transferInInfo.getInvestAmount();
        this.yearYield=transferInInfo.getYearYield();
        this.surplusDays=transferInInfo.getSurplusDays();
        
        this.nextRepaymentDate=transferInInfo.getNextRepaymentDate()!=null?transferInInfo.getNextRepaymentDate().getTime()/1000:null;
        this.applyTime=transferInInfo.getApplyTime()!=null?transferInInfo.getApplyTime().getTime()/1000:null;
        this.successTime=transferInInfo.getSuccessTime()!=null?transferInInfo.getSuccessTime().getTime()/1000:null;
        this.actualRepaymentDate=transferInInfo.getActualRepaymentDate()!=null?transferInInfo.getActualRepaymentDate().getTime()/1000:null;
    	
    
    }

	public Integer getCreditId() {
		return creditId;
	}

	public void setCreditId(Integer creditId) {
		this.creditId = creditId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Long purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public Integer getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(Integer creditStatus) {
		this.creditStatus = creditStatus;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public Double getYearYield() {
		return yearYield;
	}

	public void setYearYield(Double yearYield) {
		this.yearYield = yearYield;
	}

	public Integer getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(Integer surplusDays) {
		this.surplusDays = surplusDays;
	}

	public BigDecimal getExpectedProfit() {
		return expectedProfit;
	}

	public void setExpectedProfit(BigDecimal expectedProfit) {
		this.expectedProfit = expectedProfit;
	}

	public Long getNextRepaymentDate() {
		return nextRepaymentDate;
	}

	public void setNextRepaymentDate(Long nextRepaymentDate) {
		this.nextRepaymentDate = nextRepaymentDate;
	}

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



}
