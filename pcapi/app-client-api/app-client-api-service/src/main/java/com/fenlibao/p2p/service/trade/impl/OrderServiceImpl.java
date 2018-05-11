package com.fenlibao.p2p.service.trade.impl;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.trade.IOrderDao;
import com.fenlibao.p2p.service.trade.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService {

	@Resource
	private IOrderDao orderDao;
	
	@Override
	public Timestamp getCreateTimeByOrderId(int orderId) throws Exception {
		return orderDao.getCreateTimeByOrderId(orderId);
	}

	@Override
	public Integer getUserIdByOrderId(int orderId) {
		return orderDao.getUserIdByOrderId(orderId);
	}

	@Override
	public int insertOrderIdAndClientType(int orderId, String clientType) {
		return orderDao.insertOrderIdAndClientType(orderId, clientType);
	}

}
