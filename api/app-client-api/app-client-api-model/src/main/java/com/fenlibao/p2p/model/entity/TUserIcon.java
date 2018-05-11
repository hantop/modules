package com.fenlibao.p2p.model.entity;

public class TUserIcon {

	private int id;
	
	private int type;
	
	private String picFilename;
	
	private String picUrl;
	
	private int userId;
	
	private int count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPicFilename() {
		return picFilename;
	}

	public void setPicFilename(String picFilename) {
		this.picFilename = picFilename;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
