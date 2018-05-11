package com.fenlibao.p2p.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.model.entity.bid.BidFiles;

@Repository
public class PublicAccessoryDaoImpl implements PublicAccessoryDao{

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "PublicAccessoryMapper.";
	
	@Override
	public List<T6232> getPublicAccessory(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getPublicAccessory", map);
	}

	@Override
	public List<BidFiles> getPublicAccessoryFiles(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getPublicAccessoryFiles", map);
	}

}
