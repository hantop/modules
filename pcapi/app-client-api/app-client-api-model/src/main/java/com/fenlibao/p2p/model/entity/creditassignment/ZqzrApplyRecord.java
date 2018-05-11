/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: ZqzrApplyRecord.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.entity.creditassignment 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-4 下午6:45:54 
 * @version: V1.1   
 */
package com.fenlibao.p2p.model.entity.creditassignment;

/** 
 * @ClassName: ZqzrApplyRecord 
 * @Description: 债权转让申请记录
 * @author: laubrence
 * @date: 2015-11-4 下午6:45:54  
 */
public class ZqzrApplyRecord {
	
	public int zqId;
	
	public int zqApplyId;
	
	public int userId;

	/**
	 * @return the zqId
	 */
	public int getZqId() {
		return zqId;
	}

	/**
	 * @param zqId the zqId to set
	 */
	public void setZqId(int zqId) {
		this.zqId = zqId;
	}

	/**
	 * @return the zqApplyId
	 */
	public int getZqApplyId() {
		return zqApplyId;
	}

	/**
	 * @param zqApplyId the zqApplyId to set
	 */
	public void setZqApplyId(int zqApplyId) {
		this.zqApplyId = zqApplyId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
