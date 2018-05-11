package com.fenlibao.p2p.model.entity.activity;

import java.io.Serializable;

/**
 * 8月好友邀请活动用户的邀请好友
 */
public class VirusSpreadFreinds implements Serializable{

	private static final long serialVersionUID = 1L;

	private String phone;//手机号
	
	private String avatar;//头像

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
