package com.fenlibao.p2p.model.entity.invite;

import java.math.BigDecimal;
import java.util.Date;

/** 
 * @author: junda.feng
 * @date: 2016-6-20
 */
public class MyAwordInfo {
	
	public String realname;//姓名
	
	public String phonenum;//手机号码
	
	public BigDecimal investAmount;//投资金额
	
	public BigDecimal award;//奖励金额
	
	public Date countDate;//统计时间

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public BigDecimal getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(BigDecimal investAmount) {
		this.investAmount = investAmount;
	}

	public BigDecimal getAward() {
		return award;
	}

	public void setAward(BigDecimal award) {
		this.award = award;
	}

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	
}

