package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by laubrence on 2016/3/28.
 */
public class InvestInfoDetail extends InvestInfo {

	/* 借款描述 */
	String remark;

	/* 投资时间 */
	Date investTime;

	/* 计息时间 */
	Date interestTime;

	/* 到期时间 */
	Date expireTime;

	// 2016-06-28 junda.feng
	private String repaymentMode;// 还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清)
									
	private String interestPaymentType;// 付息方式,ZRY:自然月;GDR:固定日;

	private Date applyTime;//债权转让申请时间
	private Date successTime;//转让成功日
	private Date actualRepaymentDate;//到期回款日
	private BigDecimal interestRiseAmount;//加息奖励

	private String  bidOrigin;//标的来源：（分利宝：0001	缺钱么：0002）

	private double interestRise;//加息券加息利率
	private double bidInterestRise;//加息标加息利率
	private int anytimeQuit;//随时退出标：1是、0否
	private int cgNum;  //1：普通标，2：存管标

	public String getBidOrigin() {
		return bidOrigin;
	}

	public void setBidOrigin(String bidOrigin) {
		this.bidOrigin = bidOrigin;
	}

	public BigDecimal getInterestRiseAmount() {
		return interestRiseAmount;
	}

	public void setInterestRiseAmount(BigDecimal interestRiseAmount) {
		this.interestRiseAmount = interestRiseAmount;
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

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getInterestPaymentType() {
		return interestPaymentType;
	}

	public void setInterestPaymentType(String interestPaymentType) {
		this.interestPaymentType = interestPaymentType;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Date getInterestTime() {
		return interestTime;
	}

	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}

	public Date getInvestTime() {
		return investTime;
	}

	public void setInvestTime(Date investTime) {
		this.investTime = investTime;
	}

	@Override
	public double getInterestRise() {
		return interestRise;
	}

	@Override
	public void setInterestRise(double interestRise) {
		this.interestRise = interestRise;
	}

	@Override
	public double getBidInterestRise() {
		return bidInterestRise;
	}

	@Override
	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
	}

	@Override
	public int getAnytimeQuit() {
		return anytimeQuit;
	}

	@Override
	public void setAnytimeQuit(int anytimeQuit) {
		this.anytimeQuit = anytimeQuit;
	}

	public int getCgNum() {
		return cgNum;
	}

	public void setCgNum(int cgNum) {
		this.cgNum = cgNum;
	}
}
