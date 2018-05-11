package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.UserSecurityAuthentication;

import java.util.Map;

public interface UserSecurityDao {

	public void updateUserSecurity(Map map);
	
	UserSecurityAuthentication getUserSecurity(String userId);

	/**
	 * 获取用户申请绑卡成功审核通过的次数
	 * @param userId
	 * @param userRole
	 * @return
	 */
	Integer getApplyUnbindTimes(Integer userId, String userRole);
}
