package com.fenlibao.p2p.model.entity.activity;

import java.io.Serializable;

/**
 * 年会指定中奖名单
 */
public class AnnualMeetingDesignated implements Serializable{
	private String name;
	private String phone;
	private String realFlag;//0假数据1真实数据
	private String prizeCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealFlag() {
		return realFlag;
	}

	public void setRealFlag(String realFlag) {
		this.realFlag = realFlag;
	}

	public String getPrizeCode() {
		return prizeCode;
	}

	public void setPrizeCode(String prizeCode) {
		this.prizeCode = prizeCode;
	}
}
