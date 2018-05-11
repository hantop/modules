/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: AccessToken.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.thirdparty 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-11 下午7:21:49 
 * @version: V1.1   
 */
package com.fenlibao.p2p.service.thirdparty;

import com.fenlibao.p2p.model.entity.AccessToken;

/**
 * @ClassName: AccessToken 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-11 下午7:21:49  
 */
public interface WebAccessService {

	/**
	 * 创建验证标识码
	 * @param userId
	 * @param clientType
	 * @param targetClientType
	 * @return
     * @throws Exception
     */
	String buildAccessToken(int userId, String clientType, String targetClientType) throws Exception;

	/**
	 * 获取getAccessToken
	 * @param accessToken
	 * @return
	 * @throws Exception
     */
	AccessToken getAccessToken(String accessToken) throws Exception;

	int updateVaildAccessToken(int recordId);
}
