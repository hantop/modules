package com.fenlibao.p2p.model.vo.creditassignment;

import java.util.Date;

import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;

/** 
 * 可转让债权转让列表
 * @author: junda.feng
 */
public class AllowTransferInfoVO {
	private Integer bidId; //标id
    private Integer creditId; //债权id
    private String title; //债权标题
    private Date purchaseTime; //投资时间
    private Integer creditStatus; //债权状态(1:已投资2:收益中;3:转让中 4:已转出 5:已逾期 6: 已回款)
    private String investAmount; //投资金额
    private Double yearYield; //年化收益率
    private Integer periodDates;//项目期限（购买的项目或债权）
    private String expectedProfit; //预期收益
    private Date expireDate;//到期时间
	private Boolean  creditFlag;//债权还是项目
	private int anytimeQuit;//随时退出标：1是、0否 20170301

	//wxd 20161013
	private Boolean jxFlag; //是否已加息

	private double bidInterestRise;//标加息利率

    public AllowTransferInfoVO(TransferInInfo transferInInfo) {
    	this.bidId = transferInInfo.getBidId();
    	this.creditId = transferInInfo.getCreditId();
    	this.title = transferInInfo.getZrId()==0?transferInInfo.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX+transferInInfo.getCreditCode();
    	this.creditFlag = transferInInfo.getZrId()!=0;
		this.purchaseTime = transferInInfo.getHoldTime();
    	 //标的状态
		this.creditStatus = 2;
        this.investAmount = transferInInfo.getInvestAmount().toPlainString();
        this.yearYield = transferInInfo.getYearYield();
        this.periodDates = transferInInfo.getPeriodDates();
        this.expireDate = transferInInfo.getExpireDate();
        this.expectedProfit = transferInInfo.getExpectedProfit().toPlainString();
		this.jxFlag = transferInInfo.isJxFlag();
		this.anytimeQuit = transferInInfo.getAnytimeQuit();
    }


	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
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



	public Boolean getCreditFlag() {
		return creditFlag;
	}

	public void setCreditFlag(Boolean creditFlag) {
		this.creditFlag = creditFlag;
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


	public Integer getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(Integer creditStatus) {
		this.creditStatus = creditStatus;
	}

	public String getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(String investAmount) {
		this.investAmount = investAmount;
	}

	public String getExpectedProfit() {
		return expectedProfit;
	}

	public void setExpectedProfit(String expectedProfit) {
		this.expectedProfit = expectedProfit;
	}

	public Double getYearYield() {
		return yearYield;
	}

	public void setYearYield(Double yearYield) {
		this.yearYield = yearYield;
	}

	public Integer getPeriodDates() {
		return periodDates;
	}

	public void setPeriodDates(Integer periodDates) {
		this.periodDates = periodDates;
	}

	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}


	public double getBidInterestRise() {
		return bidInterestRise;
	}

	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
	}
}
