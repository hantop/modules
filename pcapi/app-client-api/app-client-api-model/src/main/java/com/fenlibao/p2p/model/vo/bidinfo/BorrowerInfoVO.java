package com.fenlibao.p2p.model.vo.bidinfo;

import java.io.Serializable;

/** 
 * @ClassName: BorrowerInfo 
 * @Description: 借款人信息
 * @author: laubrence
 * @date: 2016-3-3 下午4:23:20  
 */
public class BorrowerInfoVO implements Serializable{

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
	
	private int isHouseCertified; //是否房产认证(0:无;1:有)
	
	private int isCarCertified; //是否车产认证(0:无;1:有)。
	
	private  boolean historyInfoFlag=true;//2.0之前的数据

	
	public  boolean isHistoryInfoFlag() {
		return historyInfoFlag;
	}



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

	public int getIsHouseCertified() {
		return isHouseCertified;
	}

	public void setIsHouseCertified(int isHouseCertified) {
		this.isHouseCertified = isHouseCertified;
	}

	public int getIsCarCertified() {
		return isCarCertified;
	}

	public void setIsCarCertified(int isCarCertified) {
		this.isCarCertified = isCarCertified;
	}

}
