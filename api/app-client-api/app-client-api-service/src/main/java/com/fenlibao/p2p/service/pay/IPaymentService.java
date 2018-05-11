package com.fenlibao.p2p.service.pay;

import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;

public interface IPaymentService {

	/**
	 * 添加支付订单
	 * @param amount
	 * @param channelCode 支付通道
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PaymentOrderEntity addOrder(String amount, Integer channelCode, Integer userId) throws Exception;
	
	/**
	 * 更新订单
	 * @param order
	 * @throws Exception
	 */
	void updateOrder(PaymentOrderEntity order) throws Exception;
	
	/**
	 * 获取支付订单并锁定
	 * @param id
	 * @return
	 * @throws Exception
	 */
	PaymentOrderEntity lockOrder(Integer id) throws Exception;
	
	/**
	 * 提交订单
	 * @param order
	 * @throws Exception
	 */
	void submit(PaymentOrderEntity order) throws Exception;
	
}
