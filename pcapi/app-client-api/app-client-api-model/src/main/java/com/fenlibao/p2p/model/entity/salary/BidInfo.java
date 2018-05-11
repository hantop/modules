/**
 * 
 */
package com.fenlibao.p2p.model.entity.salary;

import java.util.Date;

/**
 * @author zhaohongfeng 2015-8-18 15:59:34
 *
 */
public class BidInfo {
	
	private int bidId;//标ID
		
	private int userId;//用户ID
	
	private String bidTitle;//借款标题
	
	private double yearRate;//年化利率
	
	private Date publishDate;//发布时间
	
	private int fundraisDay;//筹款天数
	
	private double provideAmount; //可投金额 F07
	
	private String bidStatus; //标状态
	
	private Date stopBidDay; //封标时间（筹款到期时间）
	
	private int investDay; //投资日
	
	private int loandays; //借款月数
	
	private int period;//期数
	
	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getFundraisDay() {
		return fundraisDay;
	}

	public void setFundraisDay(int fundraisDay) {
		this.fundraisDay = fundraisDay;
	}

	public double getProvideAmount() {
		return provideAmount;
	}

	public void setProvideAmount(double provideAmount) {
		this.provideAmount = provideAmount;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}

	public Date getStopBidDay() {
		return stopBidDay;
	}

	public void setStopBidDay(Date stopBidDay) {
		this.stopBidDay = stopBidDay;
	}

	public int getInvestDay() {
		return investDay;
	}

	public void setInvestDay(int investDay) {
		this.investDay = investDay;
	}

	public int getLoandays() {
		return loandays;
	}

	public void setLoandays(int loandays) {
		this.loandays = loandays;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

}
