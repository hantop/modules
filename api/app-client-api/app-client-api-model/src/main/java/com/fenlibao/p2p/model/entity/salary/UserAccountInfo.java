package com.fenlibao.p2p.model.entity.salary;

public class UserAccountInfo {
	
	private int id;//id
	
	private int userId;// 用户id
	
	private String accountType; //账户类型
	
	private double accountSum; //账户余额

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getAccountSum() {
		return accountSum;
	}

	public void setAccountSum(double accountSum) {
		this.accountSum = accountSum;
	}
}
