package com.fenlibao.p2p.util.email;

import java.util.Date;

public class EmailParam {

	private String userId;
	
	private String redirecturl;
	
	private String sign;
	
	private Date exceedTime;
	
	private int type;
	
	private String email;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRedirecturl() {
		return redirecturl;
	}

	public void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Date getExceedTime() {
		return exceedTime;
	}

	public void setExceedTime(Date exceedTime) {
		this.exceedTime = exceedTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
