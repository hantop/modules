package com.fenlibao.p2p.model.entity;

import java.util.Date;
/**
 * 薪金宝(开店宝)加入记录
 */
public class TJoinRecord {

	private int id;
	
	private int fUserId;
	
	private String fUserName;
	
	private int fType;
	
	private int fDay;
	
	private int fAmount;
	
	private Date fJoinTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getfUserId() {
		return fUserId;
	}

	public void setfUserId(int fUserId) {
		this.fUserId = fUserId;
	}

	public String getfUserName() {
		return fUserName;
	}

	public void setfUserName(String fUserName) {
		this.fUserName = fUserName;
	}

	public int getfType() {
		return fType;
	}

	public void setfType(int fType) {
		this.fType = fType;
	}

	public int getfDay() {
		return fDay;
	}

	public void setfDay(int fDay) {
		this.fDay = fDay;
	}

	public int getfAmount() {
		return fAmount;
	}

	public void setfAmount(int fAmount) {
		this.fAmount = fAmount;
	}

	public Date getfJoinTime() {
		return fJoinTime;
	}

	public void setfJoinTime(Date fJoinTime) {
		this.fJoinTime = fJoinTime;
	}
	
}
