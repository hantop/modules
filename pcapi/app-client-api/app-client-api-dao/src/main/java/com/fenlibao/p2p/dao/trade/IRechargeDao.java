package com.fenlibao.p2p.dao.trade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;

public interface IRechargeDao {

	/**
	 * 获取所有处于“待确认”状态的充值订单
	 * @return
	 */
	List<RechargeOrder> getDQROrder();
	
	/**
	 * 更新订单的状态
	 * @param orderId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	int updateOrderStatus(Map<String, Object> params) throws Exception;
	
	/**
	 * 获取某个用户 n小时内 线下充值总额
	 * @param userId
	 * @param hours
	 * @return
	 */
	BigDecimal getOfflineRechargeAmount(int userId, int hours);

	/**
	 * 获取支付额度说明
	 * @param bankCode
	 * @param channelCode
     * @return
	 */
	List<PaymentLimitVO> getLimitList(String bankCode, String channelCode);

	/**
	 * 获取存管支付限额
	 * @param bankCode
	 * @return
	 */
	List<PaymentLimitVO> getCgLimit(String bankCode);
}
