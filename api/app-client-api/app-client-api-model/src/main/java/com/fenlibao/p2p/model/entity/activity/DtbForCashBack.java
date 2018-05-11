package com.fenlibao.p2p.model.entity.activity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 地铁报活动-返现用户
 */
public class DtbForCashBack implements Serializable{

	public static String ACTIVITY_CODE="DTB_CASHBACK_ACTIVITY";

	private BigDecimal amount;//发放金额

	private Integer userId;//用户id

	private Integer recordId;//投资记录

	private String phone;//手机

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
}
