package com.fenlibao.p2p.model.entity.experiencegold;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 体验金收益实体
 * @author yangzengcai
 * @date 2015年11月18日
 */
public class ExperienceGoldEarningsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5940474838455167139L;

	private Integer userId;
	
	/**
	 * 体验金ID
	 */
	private Integer expId;
	
	/**
	 * 收益
	 */
	private BigDecimal amount;
	
	private Date createTime = new Date();
	
	/**
	 * 收益日期
	 */
	private Date earningsDate;
	
	/**
	 * 是否到账
	 */
	private String arrivalStatus = "F";

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getExpId() {
		return expId;
	}

	public void setExpId(Integer expId) {
		this.expId = expId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEarningsDate() {
		return earningsDate;
	}

	public void setEarningsDate(Date earningsDate) {
		this.earningsDate = earningsDate;
	}

	public String getArrivalStatus() {
		return arrivalStatus;
	}

	public void setArrivalStatus(String arrivalStatus) {
		this.arrivalStatus = arrivalStatus;
	}

	
}
