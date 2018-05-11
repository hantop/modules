package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信验证码
 *
 */
public class SmsValidcode implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String phoneNum;
	
	private String validCode;
	
	private int sendType;
	
	private Date outTime;

	private String userIp;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	
}
