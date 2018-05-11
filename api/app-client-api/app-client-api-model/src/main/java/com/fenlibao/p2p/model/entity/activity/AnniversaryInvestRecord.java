package com.fenlibao.p2p.model.entity.activity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 1周年活动投资记录表
 */
public class AnniversaryInvestRecord implements Serializable{
	private String phone;//手机
	private String sumAmout;//投资总额
	public String getSumAmout() {
		return sumAmout;
	}

	public void setSumAmout(String sumAmout) {
		this.sumAmout = sumAmout;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
