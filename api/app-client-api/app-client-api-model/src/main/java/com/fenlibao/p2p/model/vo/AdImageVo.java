package com.fenlibao.p2p.model.vo;

/**
 * 广告图信息
 *
 */
public class AdImageVo {

	private String adPicUrl;//广告图片
	
	private String adWebUrl;//广告链接

	private int loginFlag; //登录标识

	public String getAdPicUrl() {
		return adPicUrl;
	}

	public void setAdPicUrl(String adPicUrl) {
		this.adPicUrl = adPicUrl;
	}

	public String getAdWebUrl() {
		return adWebUrl;
	}

	public void setAdWebUrl(String adWebUrl) {
		this.adWebUrl = adWebUrl;
	}

	public int getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(int loginFlag) {
		this.loginFlag = loginFlag;
	}
}
