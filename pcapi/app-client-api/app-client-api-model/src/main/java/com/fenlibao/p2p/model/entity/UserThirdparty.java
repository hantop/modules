package com.fenlibao.p2p.model.entity;

/**
 *  第三方登录信息
 */
public class UserThirdparty {

	private int userId;
	
	private String openId;
	
	private String token;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
