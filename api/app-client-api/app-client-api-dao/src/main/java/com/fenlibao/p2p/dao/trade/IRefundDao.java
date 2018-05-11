package com.fenlibao.p2p.dao.trade;

import java.util.List;

import com.fenlibao.p2p.model.entity.pay.RefundOrderEntity;
import com.fenlibao.p2p.model.vo.pay.RefundVO;

public interface IRefundDao {

	/**
	 * 添加退款订单
	 * @param order
	 * @throws Exception
	 */
	void addOrder(RefundOrderEntity order) throws Exception;
	
	/**
	 * 更新退款订单
	 * @param order
	 * @throws Exception
	 */
	void updateOrder(RefundOrderEntity order) throws Exception;
	
	/**
	 * 获取退款订单并锁定
	 * @param orderId
	 * @return
	 */
	RefundOrderEntity lockOrder(Integer orderId);
	
	/**
	 * 获取待退款的订单
	 * @return
	 */
	List<RefundVO> getWaitRefundOrder();
	
}
