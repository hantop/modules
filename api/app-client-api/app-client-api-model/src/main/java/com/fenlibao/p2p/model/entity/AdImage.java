package com.fenlibao.p2p.model.entity;

import java.io.Serializable;

/**
 * 广告图(ad_image)
 */
public class AdImage implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;
	
	private String picUrl;//图片路径
	
	private int priority;//顺序
	
	private String responseLink;//广告图的广告链接

	private int loginFlag;//是否需要登录标识
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getResponseLink() {
		return responseLink;
	}

	public void setResponseLink(String responseLink) {
		this.responseLink = responseLink;
	}

	public int getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(int loginFlag) {
		this.loginFlag = loginFlag;
	}
}
