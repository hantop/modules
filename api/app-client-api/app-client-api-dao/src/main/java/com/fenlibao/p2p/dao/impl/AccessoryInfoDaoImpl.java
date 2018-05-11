package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.AccessoryInfoDao;
import com.fenlibao.p2p.model.entity.AccessoryInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AccessoryInfoDaoImpl implements AccessoryInfoDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "AccessoryInfoMapper.";
	
	@Override
	public AccessoryInfo getAccessoryInfo(int id) {
		return sqlSession.selectOne(MAPPER+"getAccessoryInfo", id);
	}

	@Override
	public int insertAccessoryInfo(AccessoryInfo info) {
		return sqlSession.insert(MAPPER+"insertAccessoryInfo", info);
	}

}
