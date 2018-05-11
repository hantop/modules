/**   
 * Copyright © 2015 公司名. All rights reserved.
 * 
 * @Title: WeixinStatusVO.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2015-10-13 下午3:46:44 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.vo;

/** 
 * @ClassName: WeixinStatusVO 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-13 下午3:46:44  
 */
public class WeixinStatusVO {
	
	/**
	 * @fieldName: weixinStatus
	 * @fieldType: String
	 * @Description: 微信返回状态码
	 */
	public String weixinStatus;
	
	/**
	 * @fieldName: weixinMsg
	 * @fieldType: String
	 * @Description: 微信返回状态信息
	 */
	public String weixinMsg;
	
	/**
	 * @return the weixinStatus
	 */
	public String getWeixinStatus() {
		return weixinStatus;
	}

	/**
	 * @param weixinStatus the weixinStatus to set
	 */
	public void setWeixinStatus(String weixinStatus) {
		this.weixinStatus = weixinStatus;
	}

	/**
	 * @return the weixinMsg
	 */
	public String getWeixinMsg() {
		return weixinMsg;
	}

	/**
	 * @param weixinMsg the weixinMsg to set
	 */
	public void setWeixinMsg(String weixinMsg) {
		this.weixinMsg = weixinMsg;
	}

}
