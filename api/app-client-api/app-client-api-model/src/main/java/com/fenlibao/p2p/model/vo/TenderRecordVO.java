package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 投标记录统计
 */
public class TenderRecordVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userName;//用户名
	
	private double totalInvest;//总的投资金额 

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public double getTotalInvest() {
		return totalInvest;
	}

	public void setTotalInvest(double totalInvest) {
		this.totalInvest = totalInvest;
	}
}
