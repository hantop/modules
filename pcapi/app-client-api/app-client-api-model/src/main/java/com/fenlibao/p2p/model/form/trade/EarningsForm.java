package com.fenlibao.p2p.model.form.trade;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收益form
 * <p>用于定时任务统计每个用户每个标的预期收益
 * @author yangzengcai
 * @date 2015年10月16日
 */
public class EarningsForm {
	
	/**
	 * 用户ID
	 */
	private Integer userId;
	
	/**
	 * 标ID
	 */
	private Integer bidId;
	
	/**
	 * 标类型ID
	 */
	private Integer bidTypeId;
	
	/**
	 * 用户某个标的预期总收益
	 */
	private BigDecimal amount;
	
	/**
	 * 计息开始时间
	 * [当天产生收益]
	 */
	private Date jx_startTime;
	
	/**
	 * 计息结束时间（标的到期时间）
	 * [当天没有收益]
	 */
	private Date jx_endTime;
	
	/**
	 * 债权ID
	 */
	private Integer ZQ_id;
	/**
	 * 债权转让订单ID
	 * <p>如果债权发生过转让改值会大于 0 
	 */
	private Integer ZQZR_orderId;
	
	private Integer period; //期数

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getZQ_id() {
		return ZQ_id;
	}

	public void setZQ_id(Integer zQ_id) {
		ZQ_id = zQ_id;
	}

	public Integer getZQZR_orderId() {
		return ZQZR_orderId;
	}

	public void setZQZR_orderId(Integer zQZR_orderId) {
		ZQZR_orderId = zQZR_orderId;
	}

	public Date getJx_startTime() {
		return jx_startTime;
	}

	public void setJx_startTime(Date jx_startTime) {
		this.jx_startTime = jx_startTime;
	}

	public Date getJx_endTime() {
		return jx_endTime;
	}

	public void setJx_endTime(Date jx_endTime) {
		this.jx_endTime = jx_endTime;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}
	
}
