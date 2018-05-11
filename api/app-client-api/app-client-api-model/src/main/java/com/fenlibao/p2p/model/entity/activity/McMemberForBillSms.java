package com.fenlibao.p2p.model.entity.activity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 9月名创会员首投活动-送话费发短信
 */
public class McMemberForBillSms implements Serializable{

	private Integer userId;//用户id
	
	private Integer recordId;//投资记录

	private String phone;//手机

	private Integer parvalue;//赠送话费面额

	public Integer getParvalue() {
		return parvalue;
	}

	public void setParvalue(Integer parvalue) {
		this.parvalue = parvalue;
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
