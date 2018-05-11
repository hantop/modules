package com.fenlibao.platform.dao.thirdparty;

import org.apache.ibatis.annotations.Param;

import com.fenlibao.platform.model.thirdparty.entity.TPUserEntity;

public interface TPUserMapper {

	/**
	 * 获取用户信息
	 * @param username
	 * @param password
	 * @return
	 */
	TPUserEntity get(@Param("username")String username, @Param("password")String password);

	String getResourceURI(String username);
}
