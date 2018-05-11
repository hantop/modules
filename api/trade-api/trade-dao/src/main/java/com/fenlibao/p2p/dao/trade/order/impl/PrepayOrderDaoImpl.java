package com.fenlibao.p2p.dao.trade.order.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.trade.order.PrepayOrderDao;
import com.fenlibao.p2p.model.trade.entity.order.T6521;

@Repository
public class PrepayOrderDaoImpl implements PrepayOrderDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "PrepayOrderMapper.";
	
	@Override
	public void add(T6521 t6521) throws Exception {
		sqlSession.insert(MAPPER + "add", t6521);
	}

	@Override
	public T6521 get(int id) throws Exception {
		return sqlSession.selectOne(MAPPER+"get", id);
	}

}
