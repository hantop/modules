package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.UserSecurityDao;
import com.fenlibao.p2p.model.entity.UserSecurityAuthentication;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Repository
public class UserSecurityDaoImpl implements UserSecurityDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "UserSecurityMapper.";
	
	@Override
	public void updateUserSecurity(Map map) {
		sqlSession.update(MAPPER+"updateUserSecurity", map);
	}

	@Override
	public UserSecurityAuthentication getUserSecurity(String userId) {
		return sqlSession.selectOne(MAPPER+"getUserSecurity", userId);
	}

	@Override
	public Integer getApplyUnbindTimes(Integer userId, String userRole) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("userRole", userRole);
		return sqlSession.selectOne(MAPPER+"getApplyUnbindTimes",params);
	}
}
