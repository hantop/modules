package com.fenlibao.p2p.model.entity.salary;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhaohongfeng 2015-8-18 15:59:34
 *
 */


public class UserXjbBidInfo {
	
	private int id;//用户薪金宝计划id
		
	private String bidTitle;//借款标题
	
	private double yearRate;//年化利率
	
	private int loandays; //借款月数
	
	private Date publishTime;//发布时间
	
	private Date userJoinTime; //用户加入时间
	
	private Date planStopTime; //计划到期时间
	
	private BigDecimal monthAmount; //每月投资金额
		
	private int investDay;//投资日

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBidTitle() {
		return bidTitle;
	}

	public void setBidTitle(String bidTitle) {
		this.bidTitle = bidTitle;
	}

	public double getYearRate() {
		return yearRate;
	}

	public void setYearRate(double yearRate) {
		this.yearRate = yearRate;
	}

	public int getLoandays() {
		return loandays;
	}

	public void setLoandays(int loandays) {
		this.loandays = loandays;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getUserJoinTime() {
		return userJoinTime;
	}

	public void setUserJoinTime(Date userJoinTime) {
		this.userJoinTime = userJoinTime;
	}

	public Date getPlanStopTime() {
		return planStopTime;
	}

	public void setPlanStopTime(Date planStopTime) {
		this.planStopTime = planStopTime;
	}

	public BigDecimal getMonthAmount() {
		return monthAmount;
	}

	public void setMonthAmount(BigDecimal monthAmount) {
		this.monthAmount = monthAmount;
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
