package com.fenlibao.p2p.dao.trade;

import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;

/**
 * 支付
 * @author zcai
 * @date 2016年4月20日
 */
public interface IPaymentDao {

	/**
	 * 添加支付订单
	 * @param order
	 * @throws Exception
	 */
	void addOrder(PaymentOrderEntity order) throws Exception;
	
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
	
}
