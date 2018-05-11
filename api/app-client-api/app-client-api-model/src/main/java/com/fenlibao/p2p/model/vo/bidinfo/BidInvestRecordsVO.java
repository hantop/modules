package com.fenlibao.p2p.model.vo.bidinfo;

import java.io.Serializable;

/**
 * 标的投资记录
 */
public class BidInvestRecordsVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private long investTime;//投资时间
	
	private String investorName;//投资人姓名
	
	private double investAmount;//投资金额

	private String phone;//投资人手机

	private String interest;//加息利率

	private int isCG;//是否是存管类型的标 2：是 1：不是

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getInvestTime() {
		return investTime;
	}

	public void setInvestTime(long investTime) {
		this.investTime = investTime;
	}

	public String getInvestorName() {
		return investorName;
	}

	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}

	public double getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(double investAmount) {
		this.investAmount = investAmount;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public int getIsCG() {
		return isCG;
	}

	public void setIsCG(int isCG) {
		this.isCG = isCG;
	}
}
