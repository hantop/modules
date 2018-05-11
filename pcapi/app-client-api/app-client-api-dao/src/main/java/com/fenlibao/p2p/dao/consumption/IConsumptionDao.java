package com.fenlibao.p2p.dao.consumption;

import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;

public interface IConsumptionDao {

	/**
	 * 添加消费订单
	 * @param order
	 * @throws Exception
	 */
	void addOrder(ConsumptionOrderEntity order) throws Exception;
	
	/**
	 * 更新订单
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
	
	/**
	 * 获取某个支付订单所有的消费订单
	 * <p>当一个支付订单对应多个消费订单的时候不能使用</p>
	 * @param paymentOrderId
	 * @return
	 */
	ConsumptionOrderEntity getOrderByPaymentId(Integer paymentOrderId);
	
}
