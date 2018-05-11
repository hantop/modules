package com.fenlibao.p2p.dao.creditassignment.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.creditassignment.CreditAssigmentDao;
import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;

@Repository
public class CreditAssigmentDaoImpl implements CreditAssigmentDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "CreditassignmentMapper.";
	
	@Override
	public List<Zqzrlb> getCreditassignmentApplyforList(Map<String, Object> map) {
		return sqlSession.selectList(MAPPER+"getCreditassignmentApplyforList", map);
	}

	@Override
	public int getCreditassignmentCount(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"getCreditassignmentCount", map);
	}

	@Override
	public int getCreditassignmentApplyforCount(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"getCreditassignmentApplyforCount", map);
	}

	@Override
	public int getRecord(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"getRecord", map);
	}

}
