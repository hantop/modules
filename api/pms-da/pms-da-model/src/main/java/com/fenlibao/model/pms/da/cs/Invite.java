package com.fenlibao.model.pms.da.cs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Invite {
	private Integer beinvitedId;// 被邀请用户Id
	private String phoneNum;
	private String userName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date regTime;
	private BigDecimal hasBeenInvest;
	private BigDecimal investingMoney;

	public Integer getBeinvitedId() {
		return beinvitedId;
	}

	public void setBeinvitedId(Integer beinvitedId) {
		this.beinvitedId = beinvitedId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public BigDecimal getHasBeenInvest() {
		return hasBeenInvest;
	}

	public void setHasBeenInvest(BigDecimal hasBeenInvest) {
		this.hasBeenInvest = hasBeenInvest;
	}

	public BigDecimal getInvestingMoney() {
		return investingMoney;
	}

	public void setInvestingMoney(BigDecimal investingMoney) {
		this.investingMoney = investingMoney;
	}

}
