package com.fenlibao.p2p.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.entity.PlatformUser;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.UserBaseInfoDao;
import com.fenlibao.p2p.model.entity.UserBaseInfo;
import com.fenlibao.p2p.model.vo.user.EnterpriseBaseInfoVO;

@Repository
public class UserBaseInfoDaoImpl implements UserBaseInfoDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "UserBaseInfoMapper.";
	
	@Override
	public void updateUserBaseInfo(Map map) {
		sqlSession.update(MAPPER+"updateUserBaseInfo", map);
	}

	@Override
	public int addUserBaseInfo(Map<String, Object> map) {
		return sqlSession.insert(MAPPER+"addUserBaseInfo", map);
	}

	@Override
	public UserBaseInfo getBaseInfo(Integer userId) {
		return sqlSession.selectOne(MAPPER + "getBaseInfo", userId);
	}

	@Override
	public EnterpriseBaseInfoVO getEnterpriseBaseInfo(Integer userId) {
		return sqlSession.selectOne(MAPPER + "getEnterpriseBaseInfo", userId);
	}

	@Override
	public List<PlatformUser> getPlatformUserNo(String status,int limit) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("limit", limit);
		return sqlSession.selectList(MAPPER + "getPlatformUserNo", map);
	}
}
