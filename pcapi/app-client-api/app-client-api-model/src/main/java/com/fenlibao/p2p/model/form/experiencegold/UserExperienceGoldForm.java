package com.fenlibao.p2p.model.form.experiencegold;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户体验金
 * @author yangzengcai
 * @date 2015年11月18日
 */
public class UserExperienceGoldForm {

	private Integer userId;
	
	/**
	 * 用户体验金ID
	 */
	private Integer expId;
	
	/**
	 * 收益率
	 */
	private BigDecimal yield;
	
	/**
	 * 体验金额
	 */
	private BigDecimal amount;
	
	/**
	 * 体验金有效天数
	 */
	private Integer validDate;
	
	/**
	 * 体验金开始时间
	 */
	private Date beginTime;

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

	public BigDecimal getYield() {
		return yield;
	}

	public void setYield(BigDecimal yield) {
		this.yield = yield;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getValidDate() {
		return validDate;
	}

	public void setValidDate(Integer validDate) {
		this.validDate = validDate;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
}
