package com.fenlibao.p2p.model.vo.invite;

import java.math.BigDecimal;

/** 
 * @author: junda.feng
 * @date: 2016-6-20
 */
public class MyAwordInfoVO {
	
	public String realname;//姓名
	
	public String phonenum;//手机号码
	
	public BigDecimal investAmount;//投资金额
	
	public BigDecimal award;//奖励金额
	
	public Long countDate;//统计时间
	
	public boolean hasGrant;//是否发放
	
	
	public MyAwordInfoVO() {
		this.hasGrant=true;//默认发放
	}
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


	public Long getCountDate() {
		return countDate;
	}
	public void setCountDate(Long countDate) {
		this.countDate = countDate;
	}
	public boolean isHasGrant() {
		return hasGrant;
	}

	public void setHasGrant(boolean hasGrant) {
		this.hasGrant = hasGrant;
	}

	
}

