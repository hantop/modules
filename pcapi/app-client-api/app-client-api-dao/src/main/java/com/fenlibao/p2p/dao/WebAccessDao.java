/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: WebAccess.java 
 * @Prject: app-client-api-dao
 * @Package: com.fenlibao.p2p.dao 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-11 下午7:35:19 
 * @version: V1.1   
 */
package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.AccessToken;

import java.util.Date;

/** 
 * @ClassName: WebAccess 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-11 下午7:35:19  
 */
public interface WebAccessDao {

    /** 
     * @Title: insertAccessToken 
     * @Description: TODO
	 * @param userId
	 * @param token
	 * @param clientType
	 * @param targetClientType
	 * @param createTime
	 * @param expireTime  @return
	 * @return: int
     */
    public int insertAccessToken(int userId, String token, String clientType, String targetClientType, Date createTime, Date expireTime);

	/**
	 * 获取用户getAccessToken
	 * @param accessToken
	 * @return
     */
	AccessToken getAccessToken(String accessToken);

	/**
	 * 更新accessToken已使用
	 * @param recordId
	 * @param isVerified
	 * @param nowDatetime
     * @return
     */
	int updateVaildAccessToken(int recordId, int isVerified, Date nowDatetime);
}
