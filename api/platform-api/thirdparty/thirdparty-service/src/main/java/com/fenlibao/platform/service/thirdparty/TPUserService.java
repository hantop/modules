package com.fenlibao.platform.service.thirdparty;

import com.fenlibao.platform.common.exception.BusinessException;

public interface TPUserService {

	/**
	 * 获取token
	 * @param username
	 * @param password
	 * @return
	 * @throws BusinessException
     */
	String getToken(String username, String password) throws BusinessException;
	
	/**
	 * 校验token
	 * @param token
	 * @throws BusinessException
	 */
	boolean validateToken(String token, String uri);
	
}
