package com.fenlibao.p2p.model.vo.creditassignment;

import java.util.Date;

import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.global.InterfaceConst;

/** 
 * 转让成功的债权
 * @author: junda.feng
 */
public class SuccessTransferInfoVO {
	private Integer bidId; //标id
    private Integer creditId; //债权id
    private String title; //债权标题
    private Date purchaseTime; //投资时间
    private Integer creditStatus; //债权状态(1:已投资2:收益中;3:转让中 4:已转让 5:已逾期 6: 已回款)
    private String transferAmount; //投资金额
    private Double yearYield; //年化收益率
    private Integer surplusDays;//剩余天数
    private String arrivalProfit; //已获收益
    private Date successTime;//转让成功时间
	private Boolean  creditFlag;//债权还是项目
	private Boolean jxFlag; // 是否已加息

	private double bidInterestRise;//标加息利率

    public SuccessTransferInfoVO(TransferInInfo transferInInfo) {
    	this.bidId=transferInInfo.getBidId();
    	this.creditId=transferInInfo.getCreditId();
    	this.title=transferInInfo.getZrId()==0?transferInInfo.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX+transferInInfo.getCreditCode();
		this.creditFlag=transferInInfo.getZrId()!=0;
		this.purchaseTime=transferInInfo.getHoldTime();
    	this.creditStatus=4;
    	this.arrivalProfit=transferInInfo.getArrivalProfit().toPlainString();
        this.transferAmount=transferInInfo.getTransferAmount();
        this.yearYield=transferInInfo.getYearYield();
        this.surplusDays=transferInInfo.getSurplusDays()<0?0:transferInInfo.getSurplusDays();
        this.successTime=transferInInfo.getSuccessTime();
		this.jxFlag=transferInInfo.isJxFlag();
    }

	public Boolean getJxFlag() {
		return jxFlag;
	}

	public void setJxFlag(Boolean jxFlag) {
		this.jxFlag = jxFlag;
	}

	public Integer getBidId() {
		return bidId;
	}


	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}


	public Integer getCreditId() {
		return creditId;
	}

	public Boolean getCreditFlag() {
		return creditFlag;
	}

	public void setCreditFlag(Boolean creditFlag) {
		this.creditFlag = creditFlag;
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



	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public Integer getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(Integer creditStatus) {
		this.creditStatus = creditStatus;
	}


	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}

	
	public String getArrivalProfit() {
		return arrivalProfit;
	}

	public void setArrivalProfit(String arrivalProfit) {
		this.arrivalProfit = arrivalProfit;
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


	public double getBidInterestRise() {
		return bidInterestRise;
	}

	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
	}
}
