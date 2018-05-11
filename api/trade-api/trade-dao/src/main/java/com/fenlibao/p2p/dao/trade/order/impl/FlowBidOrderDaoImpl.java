package com.fenlibao.p2p.dao.trade.order.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.trade.order.FlowBidOrderDao;
import com.fenlibao.p2p.model.trade.entity.order.T6508;

@Repository
public class FlowBidOrderDaoImpl implements FlowBidOrderDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "FlowBidOrderMapper.";
	
	@Override
	public void add(T6508 t6508)  throws Exception{
		sqlSession.insert(MAPPER + "add", t6508);
	}

	@Override
	public T6508 get(int id) throws Exception {
		return sqlSession.selectOne(MAPPER + "get", id);
	}

}
