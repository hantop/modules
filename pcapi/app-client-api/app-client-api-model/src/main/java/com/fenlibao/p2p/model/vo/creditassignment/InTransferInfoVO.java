package com.fenlibao.p2p.model.vo.creditassignment;

import java.util.Date;

import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;

/** 
 * 正在转让中债权
 * @author: junda.feng
 */
public class InTransferInfoVO {
	private Integer bidId; //标id
    private Integer creditId; //债权id
    private String title; //债权标题
    private Date purchaseTime; //投资时间
    private Integer creditStatus; //债权状态(1:已投资2:收益中;3:转让中 4:已转出 5:已逾期 6: 已回款)
    private String transferAmount;//转让价格
    private Double yearYield; //年化收益率
    private Integer surplusDays;//剩余天数
    private String investAmount; //债权金额
    private Date applyTime;//转让申请时间
	private Boolean  creditFlag;//债权还是项目
	//wxd 20161013
	private Boolean jxFlag; //是否已加息

	private double bidInterestRise;//标加息利率
    public InTransferInfoVO(TransferInInfo transferInInfo) {
    	this.bidId=transferInInfo.getBidId();
    	this.creditId=transferInInfo.getCreditId();
    	this.title=transferInInfo.getZrId()==0?transferInInfo.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX+transferInInfo.getCreditCode();
		this.creditFlag=transferInInfo.getZrId()!=0;
		this.purchaseTime=transferInInfo.getHoldTime();
    	this.creditStatus=3;
        this.transferAmount=transferInInfo.getTransferAmount();
        this.yearYield=transferInInfo.getYearYield();
        this.surplusDays=transferInInfo.getSurplusDays()<0?0:transferInInfo.getSurplusDays();
        this.investAmount=transferInInfo.getInvestAmount().toPlainString();
        this.applyTime=transferInInfo.getApplyTime();
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



	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
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

	public Double getYearYield() {
		return yearYield;
	}

	public String getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(String investAmount) {
		this.investAmount = investAmount;
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
