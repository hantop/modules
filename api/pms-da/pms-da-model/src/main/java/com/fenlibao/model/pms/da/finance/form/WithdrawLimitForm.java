package com.fenlibao.model.pms.da.finance.form;

import java.math.BigDecimal;

/**
 * 用户提现金额限制form
 * 
 */
public class WithdrawLimitForm {

	private int userId;// 用户id

	private String phoneNum;// 用户手机号
	
	private String operator;// 操作人
	
	private BigDecimal limitMoney;// 限制金额
	
	private String remark;// 备注
	
	private String startTime;// 操作开始时间
	
	private String endTime;// 操作结束时间

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public BigDecimal getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(BigDecimal limitMoney) {
		this.limitMoney = limitMoney;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
