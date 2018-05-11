package com.fenlibao.p2p.dao;

import java.util.Map;

import com.fenlibao.p2p.model.entity.UserBaseInfo;
import com.fenlibao.p2p.model.vo.user.EnterpriseBaseInfoVO;

public interface UserBaseInfoDao {

	public void updateUserBaseInfo(Map map);

	/**
	 * 新增用户基础信息
	 * @param map
	 * @return
	 */
	int addUserBaseInfo(Map<String, Object> map);
	
	/**
	 * 获取用户基础信息（姓名身份证、实名认证状态）
	 * @param userId
	 * @return
	 */
	UserBaseInfo getBaseInfo(Integer userId);
	/**
	 * 获取企业基础信息
	 * @param userId
	 * @return
	 */
	EnterpriseBaseInfoVO getEnterpriseBaseInfo(Integer userId);
}
