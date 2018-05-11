package com.fenlibao.p2p.dao.consumption.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.consumption.IConsumptionDao;
import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;

@Repository
public class ConsumptionDaoImpl extends BaseDao implements IConsumptionDao {

	public ConsumptionDaoImpl() {
		super("ConsumptionMapper");
	}

	@Override
	public void addOrder(ConsumptionOrderEntity order) throws Exception {
		sqlSession.insert(MAPPER + "addOrder", order);
	}

	@Override
	public void updateOrder(ConsumptionOrderEntity order) throws Exception {
		sqlSession.update(MAPPER + "updateOrder", order);
	}

	@Override
	public ConsumptionOrderEntity getConsumptionOrderById(Integer orderId)
			throws Exception {
		return sqlSession.selectOne(MAPPER + "getConsumptionOrderById", orderId);
		
	}

	@Override
	public ConsumptionOrderEntity getOrderByPaymentId(Integer paymentOrderId) {
		return sqlSession.selectOne(MAPPER + "getOrderByPaymentId", paymentOrderId);
	}
	
}
