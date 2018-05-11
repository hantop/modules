/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: AccessToken.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.entity 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-11 下午7:42:10 
 * @version: V1.1   
 */
package com.fenlibao.p2p.model.entity;

import java.util.Date;

/** 
 * @ClassName: AccessToken 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-11 下午7:42:10  
 */
public class AccessToken {

	int recordId;
	
	int userId;

	int targetClientType;

	int isVerified;

	String accessToken;
	
	Date createTime;

	Date expireTime;

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTargetClientType() {
		return targetClientType;
	}

	public void setTargetClientType(int targetClientType) {
		this.targetClientType = targetClientType;
	}

	public int getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
}
