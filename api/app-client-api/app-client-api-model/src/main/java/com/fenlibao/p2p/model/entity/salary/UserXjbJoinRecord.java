package com.fenlibao.p2p.model.entity.salary;

import java.util.Date;

public class UserXjbJoinRecord {
	
	private int userId;
	
	private int type;
	
	private double amount;
	
	private int investDay; //投资日
	
	private Date firstJoinPlanDate;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getFirstJoinPlanDate() {
		return firstJoinPlanDate;
	}

	public void setFirstJoinPlanDate(Date firstJoinPlanDate) {
		this.firstJoinPlanDate = firstJoinPlanDate;
	}

	/**
	 * @return the investDay
	 */
	public int getInvestDay() {
		return investDay;
	}

	/**
	 * @param investDay the investDay to set
	 */
	public void setInvestDay(int investDay) {
		this.investDay = investDay;
	}
}
