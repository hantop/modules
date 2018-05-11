package com.fenlibao.p2p.model.form.loan;

import org.apache.commons.lang3.StringUtils;

public class LoanApplicationForm {

	private String userId;
	private String amount; //借款金额（暂冗余）
	private String amountRange; //借款范围
	private String contacts; //联系人
	private String phoneNum; //手机号码
	private String districtId; //区域ID（暂冗余）
	private String districtFullName; //区域全称
	private String annualIncome; //年收入
	private String room; //是否有房
	private String car; //是否有车
	private String code; //编码
	private String captcha; //验证码
	private String period; //借款周期

	public boolean validate() {
		return StringUtils.isNoneBlank(captcha,amountRange,period,
				phoneNum,districtFullName,annualIncome,room,car);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmountRange() {
		return amountRange;
	}

	public void setAmountRange(String amountRange) {
		this.amountRange = amountRange;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictFullName() {
		return districtFullName;
	}

	public void setDistrictFullName(String districtFullName) {
		this.districtFullName = districtFullName;
	}

	public String getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
}
