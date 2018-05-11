package com.fenlibao.p2p.model.business.sms;

public class SmsResult extends SmsVo {

	private static final long serialVersionUID = 1L;

	private int errorcode;//错误编码（非0状态都是错误信息）
	
	private String errmsg;//错误信息
	
	private String source;//错误源

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	
}
