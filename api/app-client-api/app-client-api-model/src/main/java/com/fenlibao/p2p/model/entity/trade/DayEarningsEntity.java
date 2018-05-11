package com.fenlibao.p2p.model.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 日收益记录实体
 * @author yangzengcai
 * @date 2015年10月16日
 */
public class DayEarningsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3870285057091563597L;

	private Integer id;
	
	private Integer userId;
	
	private Integer bidId;
	
	private Integer bidTypeId;
	
	/**
	 * 收益日期
	 */
	private Date earningsDate;
	/**
	 * 每天收益金额
	 */
	private BigDecimal amount;
	/**
	 * 是否到账（F:否，S:是）
	 */
	private String isArrival = "F";
	
	private Date createTime = new Date();
	
	/**
	 * 债权ID
	 */
	private Integer ZQ_id;
	/**
	 * 收益类型
	 * <p>EarningsTypeConst</p>
	 */
	private Integer type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBidId() {
		return bidId;
	}

	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}

	public Integer getBidTypeId() {
		return bidTypeId;
	}

	public void setBidTypeId(Integer bidTypeId) {
		this.bidTypeId = bidTypeId;
	}
	public Date getEarningsDate() {
		return earningsDate;
	}

	public void setEarningsDate(Date earningsDate) {
		this.earningsDate = earningsDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getIsArrival() {
		return isArrival;
	}

	public void setIsArrival(String isArrival) {
		this.isArrival = isArrival;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getZQ_id() {
		return ZQ_id;
	}

	public void setZQ_id(Integer zQ_id) {
		ZQ_id = zQ_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
