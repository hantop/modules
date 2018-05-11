package com.fenlibao.p2p.dao.trade.order.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.trade.order.RepayOrderDao;
import com.fenlibao.p2p.model.trade.entity.order.T6506;

@Repository
public class RepayOrderDaoImpl implements RepayOrderDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "RepayOrderMapper.";
	
	@Override
	public void add(T6506 t6506) throws Exception {
		sqlSession.insert(MAPPER + "add", t6506);
	}

	@Override
	public T6506 get(int id) throws Exception {
		return sqlSession.selectOne(MAPPER+"get", id);
	}

}
