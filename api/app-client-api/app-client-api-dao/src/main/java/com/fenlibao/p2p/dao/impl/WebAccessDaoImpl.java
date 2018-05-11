package com.fenlibao.p2p.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fenlibao.p2p.dao.WebAccessDao;
import com.fenlibao.p2p.model.entity.AccessToken;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class WebAccessDaoImpl implements WebAccessDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "WebAccessMapper.";

	@Override
	public int insertAccessToken(int userId, String clientType, String targetClientType, String accessToken, Date createDatetime, Date expireDatetime) {
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("clientType", clientType);
		map.put("targetClientType", targetClientType);
		map.put("accessToken", accessToken);
		map.put("createDatetime", createDatetime);
		map.put("expireDatetime", expireDatetime);
		return sqlSession.insert(MAPPER+"insertAccessToken", map);
	}

	@Override
	public AccessToken getAccessToken(String accessToken) {
		Map map = new HashMap();
		map.put("accessToken", accessToken);
		return sqlSession.selectOne(MAPPER+"getAccessToken", map);
	}

	@Override
	public int updateVaildAccessToken(int recordId, int isVerified, Date nowDatetime) {
		Map map = new HashMap();
		map.put("recordId", recordId);
		map.put("isVerified", isVerified);
		map.put("nowDatetime", nowDatetime);
		return sqlSession.update(MAPPER+"updateVaildAccessToken", map);
	}

}
