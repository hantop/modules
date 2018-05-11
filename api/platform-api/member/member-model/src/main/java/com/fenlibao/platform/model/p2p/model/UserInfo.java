package com.fenlibao.platform.model.p2p.model;

import java.io.Serializable;

/**
 * 用户信息(T6110)
 */
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;// 用户ID
	private String username;// 登录用户名
	private String password;// 登录密码
	private String userType;// 用户类型(自然人、非自然人)
	private String userStatus;// 用户状态(启用、锁定)
	private String registerOrigin;// 注册来源(注册、后台添加)
	private String guarantorFlag;// 担保方(是、否)
	private String nickName;// 用户昵称
	private String phone;// 手机号
	private String userUrl;// 用户头像
	private String balance;// 余额
	private String fullName; //姓名
	private String idCardEncrypt ; //身份证号加密
	private String idCard;//身份证号（未加密）

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdCardEncrypt() {
		return idCardEncrypt;
	}

	public void setIdCardEncrypt(String idCardEncrypt) {
		this.idCardEncrypt = idCardEncrypt;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getRegisterOrigin() {
		return registerOrigin;
	}

	public void setRegisterOrigin(String registerOrigin) {
		this.registerOrigin = registerOrigin;
	}

	public String getGuarantorFlag() {
		return guarantorFlag;
	}

	public void setGuarantorFlag(String guarantorFlag) {
		this.guarantorFlag = guarantorFlag;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
}
