package com.fenlibao.p2p.service.consumption;

import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;

public interface ConsumptionService {

	/**
	 * 添加消费订单
	 * @param order
	 * @throws Exception
	 */
	void addOrder(ConsumptionOrderEntity order) throws Exception;
	
	/**
	 * 更新消费订单状态
	 * @param order
	 * @throws Exception
	 */
	void updateOrder(ConsumptionOrderEntity order) throws Exception;
	/**
	 * 获取订单
	 * @param orderId
	 * @throws Exception
	 */
	ConsumptionOrderEntity getConsumptionOrderById(Integer orderId) throws Exception;
	
	ConsumptionOrderEntity getOrderByPaymentId(Integer paymentOrderId);
}
