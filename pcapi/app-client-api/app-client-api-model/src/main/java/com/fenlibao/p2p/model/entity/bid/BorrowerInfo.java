package com.fenlibao.p2p.model.entity.bid;

import java.io.Serializable;

/** 
 * @ClassName: BorrowerInfo 
 * @Description: 借款人信息
 * @author: laubrence
 * @date: 2016-3-3 下午4:23:20  
 */
public class BorrowerInfo implements Serializable{

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -4363808991964499994L;
	
	private String infoMsg; //借款人描述信息
	
	private String identify; //身份证号码
	
	private String phone; //手机号码
	
	private String company; //工作单位
	
	private String income; //收入描述
	
	private String isHouseCertified; //是否房产认证
	
	private String isCarCertified; //是否车产认证

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getIsHouseCertified() {
		return isHouseCertified;
	}

	public void setIsHouseCertified(String isHouseCertified) {
		this.isHouseCertified = isHouseCertified;
	}

	public String getIsCarCertified() {
		return isCarCertified;
	}

	public void setIsCarCertified(String isCarCertified) {
		this.isCarCertified = isCarCertified;
	}

}
