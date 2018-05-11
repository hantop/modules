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

	private boolean autoBidFlag;//是否自动投标

	public boolean getAutoBidFlag() {
		return autoBidFlag;
	}

	public void setAutoBidFlag(boolean autoBidFlag) {
		this.autoBidFlag = autoBidFlag;
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
	
}
