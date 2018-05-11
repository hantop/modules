package com.fenlibao.p2p.dao.trade.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.IRefundDao;
import com.fenlibao.p2p.model.entity.pay.RefundOrderEntity;
import com.fenlibao.p2p.model.vo.pay.RefundVO;

@Repository
public class RefundDaoImpl extends BaseDao implements IRefundDao {

	public RefundDaoImpl() {
		super("RefundMapper");
	}

	@Override
	public void addOrder(RefundOrderEntity order) throws Exception {
		sqlSession.insert(MAPPER + "addOrder", order);
	}

	@Override
	public void updateOrder(RefundOrderEntity order) throws Exception {
		sqlSession.update(MAPPER + "updateOrder", order);
	}

	@Override
	public RefundOrderEntity lockOrder(Integer orderId) {
		return sqlSession.selectOne(MAPPER + "lockOrder", orderId);
	}

	@Override
	public List<RefundVO> getWaitRefundOrder() {
		return sqlSession.selectList(MAPPER + "getWaitRefundOrder");
	}
	
}
