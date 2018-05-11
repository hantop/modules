package com.fenlibao.p2p.dao.trade.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.IOrderDao;

@Repository
public class OrderDaoImpl extends BaseDao implements IOrderDao {
	
	public OrderDaoImpl() {
		super("OrderMapper");
	}

	@Override
	public Timestamp getCreateTimeByOrderId(int orderId) throws Exception {
		return sqlSession.selectOne(MAPPER + "getOrderCreateTime", orderId);
	}

	@Override
	public Integer getUserIdByOrderId(int orderId) {
		return sqlSession.selectOne(MAPPER + "getUserId", orderId);
	}

	@Override
	public int insertOrderIdAndClientType(int orderId, String clientType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("clientType", clientType);
		return sqlSession.insert(MAPPER + "insertOrderIdAndClientType", params);
	}

}
