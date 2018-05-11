package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

public class SalaryJoinRecordVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String investName;//投资人姓名
	
	private double investSum;//月投资金额
	
	private long timestamp;//加入时间戳

	public String getInvestName() {
		return investName;
	}

	public void setInvestName(String investName) {
		this.investName = investName;
	}

	public double getInvestSum() {
		return investSum;
	}

	public void setInvestSum(double investSum) {
		this.investSum = investSum;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
