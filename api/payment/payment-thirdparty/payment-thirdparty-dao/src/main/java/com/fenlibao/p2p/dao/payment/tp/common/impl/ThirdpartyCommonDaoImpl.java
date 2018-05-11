package com.fenlibao.p2p.dao.payment.tp.common.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.payment.tp.common.ThirdpartyCommonDao;
import com.fenlibao.p2p.model.payment.tp.common.entity.PayExtend;

@Repository
public class ThirdpartyCommonDaoImpl implements ThirdpartyCommonDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "ThirdpartyCommonMapper.";
	
	@Override
	public PayExtend getPayExtend(int userId) {
		return sqlSession.selectOne(MAPPER + "getPayExtend", userId);
	}

	@Override
	public void insertPayExtend(PayExtend payExtend) {
		sqlSession.insert(MAPPER + "insertPayExtend", payExtend);
	}

	@Override
	public void updatePayExtend(PayExtend payExtend) {
		sqlSession.update(MAPPER + "updatePayExtend", payExtend);
	}
    
}
