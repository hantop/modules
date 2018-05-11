package com.fenlibao.p2p.model.entity;

/**
 *短信息与手机号关系表(s1._1041)
 */
public class SendSmsRecordExt {

	private int id;//SendSmsRecord ID
	
	private String phoneNum;//手机号

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	
}
