package com.fenlibao.model.pms.da.finance;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 用户提现金额限制
 * 
 */
public class WithdrawLimit {

	private int userId;
	
	private String phoneNum;//用户手机号
	
	private BigDecimal limitMoney;//限制金额
	
    private Timestamp creatTime;//操作时间
    
    private String operator;//操作人
    
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public BigDecimal getLimitMoney() {
		return limitMoney;
	}
	public void setLimitMoney(BigDecimal limitMoney) {
		this.limitMoney = limitMoney;
	}
	public Timestamp getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

}
