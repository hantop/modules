package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;

public class UserCashRedPacket {
	private Integer id;
	private Integer subjectId;
	private Integer userId;
	private String phone;
	private Integer grantId;
	private String grantStatus;
	private BigDecimal money;
	private String xwRequestStatus;
	private String msg;// 只记录错误信息

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getGrantId() {
		return grantId;
	}

	public void setGrantId(Integer grantId) {
		this.grantId = grantId;
	}

	public String getGrantStatus() {
		return grantStatus;
	}

	public void setGrantStatus(String grantStatus) {
		this.grantStatus = grantStatus;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getXwRequestStatus() {
		return xwRequestStatus;
	}

	public void setXwRequestStatus(String xwRequestStatus) {
		this.xwRequestStatus = xwRequestStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
