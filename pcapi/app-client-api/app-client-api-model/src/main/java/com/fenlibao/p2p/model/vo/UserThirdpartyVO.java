package com.fenlibao.p2p.model.vo;

/**
 *  第三方登录信息
 */
public class UserThirdpartyVO {

	private String userId;
	
	private String openId;
	
	private String token;
	

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
