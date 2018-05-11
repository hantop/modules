package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.util.Date;

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
	private String authStatus;	//实名认证状态
	private String isFirstLogin;//是否第一次登录平台
	private Date registerTime;//注册时间

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	
}
