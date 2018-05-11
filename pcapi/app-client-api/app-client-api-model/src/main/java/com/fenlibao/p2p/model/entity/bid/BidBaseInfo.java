package com.fenlibao.p2p.model.entity.bid;

/** 
 * @ClassName: BidBaseInfo 
 * @Description: 标的基本信息
 * @author: laubrence
 * @date: 2016-3-5 下午4:27:11  
 */
public class BidBaseInfo {
	
	int bidId;//标ID

	String bidTitle;//借款标题

	int bidType;//标的类型

	int loanMonths;//借款月数

	int loanDays; //借款天数

	double yearYield;//年化利率

	String repaymentMode; //还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清)

	String bidStatus; //标的状态

	String assetTypes;//资产类型

	String isNoviceBid;//是否是新手标 (S:是新手标;F:普通标)

	double bidInterestRise;//计划的加息利率

	int cgMode;//是否是存管标 （1：普通标 2：存管标）

	public String getIsNoviceBid() {
		return isNoviceBid;
	}

	public void setIsNoviceBid(String isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}

	public String getBidTitle() {
		return bidTitle;
	}

	public void setBidTitle(String bidTitle) {
		this.bidTitle = bidTitle;
	}

	public int getBidType() {
		return bidType;
	}

	public void setBidType(int bidType) {
		this.bidType = bidType;
	}

	public int getLoanDays() {
		return loanDays;
	}

	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public int getLoanMonths() {
		return loanMonths;
	}

	public void setLoanMonths(int loanMonths) {
		this.loanMonths = loanMonths;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public double getYearYield() {
		return yearYield;
	}

	public void setYearYield(double yearYield) {
		this.yearYield = yearYield;
	}

	public String getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(String assetTypes) {
		this.assetTypes = assetTypes;
	}

	public double getBidInterestRise() {
		return bidInterestRise;
	}

	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
	}

	public int getCgMode() {
		return cgMode;
	}

	public void setCgMode(int cgMode) {
		this.cgMode = cgMode;
	}
}
