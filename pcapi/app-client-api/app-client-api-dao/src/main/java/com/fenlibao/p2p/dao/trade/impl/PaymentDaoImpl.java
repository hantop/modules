package com.fenlibao.p2p.dao.trade.impl;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.IPaymentDao;
import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;

@Repository
public class PaymentDaoImpl extends BaseDao implements IPaymentDao {
	
	public PaymentDaoImpl() {
		super("PaymentMapper");
	}

	@Override
	public void addOrder(PaymentOrderEntity order) throws Exception {
		sqlSession.insert(MAPPER + "addOrder", order);
	}

	@Override
	public void updateOrder(PaymentOrderEntity order) throws Exception {
		sqlSession.update(MAPPER + "updateOrder", order);
	}

	@Override
	public PaymentOrderEntity lockOrder(Integer id) throws Exception {
		return sqlSession.selectOne(MAPPER + "lockOrder", id);
	}

}
