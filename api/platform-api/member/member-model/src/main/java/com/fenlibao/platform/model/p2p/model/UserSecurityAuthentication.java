package com.fenlibao.platform.model.p2p.model;

import java.io.Serializable;

/**
 * 用户安全认证（t6118）
 */
public class UserSecurityAuthentication implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String cardIDAuth;
	
	private String phoneAuth;
	
	private String emailAuth;
	
	private String tradPasswordAuth;
	
	private String videoAuth;
	
	private String phoneNum;
	
	private String email;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCardIDAuth() {
		return cardIDAuth;
	}

	public void setCardIDAuth(String cardIDAuth) {
		this.cardIDAuth = cardIDAuth;
	}

	public String getPhoneAuth() {
		return phoneAuth;
	}

	public void setPhoneAuth(String phoneAuth) {
		this.phoneAuth = phoneAuth;
	}

	public String getEmailAuth() {
		return emailAuth;
	}

	public void setEmailAuth(String emailAuth) {
		this.emailAuth = emailAuth;
	}

	public String getTradPasswordAuth() {
		return tradPasswordAuth;
	}

	public void setTradPasswordAuth(String tradPasswordAuth) {
		this.tradPasswordAuth = tradPasswordAuth;
	}

	public String getVideoAuth() {
		return videoAuth;
	}

	public void setVideoAuth(String videoAuth) {
		this.videoAuth = videoAuth;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhone(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
