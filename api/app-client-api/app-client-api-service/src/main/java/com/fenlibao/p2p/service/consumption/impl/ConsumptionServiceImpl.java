package com.fenlibao.p2p.service.consumption.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.consumption.IConsumptionDao;
import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;
import com.fenlibao.p2p.service.consumption.ConsumptionService;

@Service
public class ConsumptionServiceImpl implements ConsumptionService {

	@Resource
	private IConsumptionDao consumptionDao;
	
	@Override
	public void addOrder(ConsumptionOrderEntity order) throws Exception {
		consumptionDao.addOrder(order);
		
	}

	@Override
	public void updateOrder(ConsumptionOrderEntity order) throws Exception {
		consumptionDao.updateOrder(order);
	}

	@Override
	public ConsumptionOrderEntity getConsumptionOrderById(Integer orderId)
			throws Exception {
		return consumptionDao.getConsumptionOrderById(orderId);
	}

	@Override
	public ConsumptionOrderEntity getOrderByPaymentId(Integer paymentOrderId) {
		return consumptionDao.getOrderByPaymentId(paymentOrderId);
	}




	



}
