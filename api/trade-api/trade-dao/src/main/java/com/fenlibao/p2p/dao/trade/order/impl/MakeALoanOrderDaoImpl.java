package com.fenlibao.p2p.dao.trade.order.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.trade.order.MakeALoanOrderDao;
import com.fenlibao.p2p.model.trade.entity.order.T6505;

@Repository
public class MakeALoanOrderDaoImpl implements MakeALoanOrderDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "MakeALoanOrderMapper.";
	
	@Override
	public void add(T6505 t6505)  throws Exception{
		sqlSession.insert(MAPPER + "add", t6505);
	}

	@Override
	public T6505 get(int id) throws Exception {
		return sqlSession.selectOne(MAPPER+"get", id);
	}

}
