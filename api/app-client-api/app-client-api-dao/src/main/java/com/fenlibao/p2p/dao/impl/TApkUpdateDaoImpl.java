package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.TApkUpdateDao;
import com.fenlibao.p2p.model.entity.TApkUpdate;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.Map;

import javax.annotation.Resource;

@Repository
public class TApkUpdateDaoImpl implements TApkUpdateDao {

	@Resource
	private SqlSession sqlSession;

	private static final String MAPPER = "TApkUpdateMapper.";
	
	@Override
	public TApkUpdate getApk(Map<String,Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getApk",map);
	}

}
