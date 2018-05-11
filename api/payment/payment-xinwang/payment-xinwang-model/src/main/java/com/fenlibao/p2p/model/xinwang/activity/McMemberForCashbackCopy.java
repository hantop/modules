package com.fenlibao.p2p.model.xinwang.activity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 9月名创会员首投活动-返现用户
 */
public class McMemberForCashbackCopy implements Serializable{

	public static final BigDecimal amount=new BigDecimal(150);//发放金额

	private Integer userId;//用户id
	
	private Integer recordId;//投资记录

	private String phone;//手机

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
