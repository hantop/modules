package com.fenlibao.p2p.model.entity.salary;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 薪金宝
 */
public class SalaryInfo {

    private int id;//标ID
	
	private int userId;//用户ID
	
	private String name;//借款标题
	
	private double rate;//年化利率
	
	private Date publishDate;//发布时间
	
	private int fundraisDay;//筹款天数
	
	private Date fundraisDate;//筹款到期时间
	
	private BigDecimal totalSum;//总筹款金额
	
	private BigDecimal leftSum;//已投金额
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
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

	public Date getFundraisDate() {
		return fundraisDate;
	}

	public void setFundraisDate(Date fundraisDate) {
		this.fundraisDate = fundraisDate;
	}

	public BigDecimal getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}

	public BigDecimal getLeftSum() {
		return leftSum;
	}

	public void setLeftSum(BigDecimal leftSum) {
		this.leftSum = leftSum;
	}
}
