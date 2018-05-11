package com.fenlibao.p2p.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.GraphValidateCodeDao;
import com.fenlibao.p2p.model.entity.GraphValidateCode;

@Repository
public class GraphValidateCodeDaoImpl implements GraphValidateCodeDao{

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "GraphValidateCode.";
	
	@Override
	public void addGraphCode(GraphValidateCode code) {
		this.sqlSession.insert(MAPPER+"addGraphCode", code);
		
	}

	@Override
	public GraphValidateCode getGraphCode(String key) {
		return this.sqlSession.selectOne(MAPPER+"getGraphCode",key);
	}

	@Override
	public void updateGraphCode(Map<String, Object> map) {
		this.sqlSession.update(MAPPER+"updateGraphValidateCode",map);
		
	}

	
}
