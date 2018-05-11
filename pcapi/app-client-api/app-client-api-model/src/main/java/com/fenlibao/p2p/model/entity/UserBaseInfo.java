package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人基础信息(s61.t6141)
 */
public class UserBaseInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int userId;//用户ID
	
	private String userName;//姓名
	
	private String interestType;//兴趣类型,LC:投资;JK:借款
	
	private String realNameAuth;//实名认证,TG:通过;BTG:不通过;
	
	private String headPicCode;//用户头像文件编码
	
	private String cardID;//身份证号,3-18位星号替换
	
	private String cardIDEncrypt;//身份证号,加密存储,唯一
	
	private Date birthDate;//出生日期

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInterestType() {
		return interestType;
	}

	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}

	public String getRealNameAuth() {
		return realNameAuth;
	}

	public void setRealNameAuth(String realNameAuth) {
		this.realNameAuth = realNameAuth;
	}

	public String getHeadPicCode() {
		return headPicCode;
	}

	public void setHeadPicCode(String headPicCode) {
		this.headPicCode = headPicCode;
	}

	public String getCardID() {
		return cardID;
	}

	public void setCardID(String cardID) {
		this.cardID = cardID;
	}

	public String getCardIDEncrypt() {
		return cardIDEncrypt;
	}

	public void setCardIDEncrypt(String cardIDEncrypt) {
		this.cardIDEncrypt = cardIDEncrypt;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
}
