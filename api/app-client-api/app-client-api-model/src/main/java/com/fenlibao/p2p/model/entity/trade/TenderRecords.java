package com.fenlibao.p2p.model.entity.trade;

/**
 * 用户投标记录t6250 t6110
 */
public class TenderRecords {

	private int userId;//用户ID
	
	private String nickname;//用户昵称
	
	private String phone;//用户手机号
	
	private double totalInvest;//总的投资金额

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getTotalInvest() {
		return totalInvest;
	}

	public void setTotalInvest(double totalInvest) {
		this.totalInvest = totalInvest;
	}
	
	
}
