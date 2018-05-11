package com.fenlibao.p2p.service.recharge;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;

public interface IRechargeService {

	/**
	 * 获取所有处于“待确认”状态的充值订单
	 * @return
	 */
	List<RechargeOrder> getDQROrder(Date requestTime);
	
	/**
	 * 向连连查询充值结果
	 * @param list
	 * @throws Throwable
	 */
	void queryResult(List<RechargeOrder> list) throws Throwable;
	
	/**
	 * 批量更新订单的状态
	 * @param orderId
	 * @param status
	 * @return
	 * @throws Exception
	 */
	int updateOrderStatus(int orderId, String status) throws Exception;
	
	/**
	 * 用于连连充值成功后更新银行卡信息和保存协议号
	 * @param params 连连回调的参数
	 * @param userId
	 * @throws Exception
	 */
	public void perfectPayInfo(Map<String, String> params, int userId) throws Exception;
	
	/**
	 * 更新银行卡绑定状态为KTX
	 * @param userId
	 * @throws Exception
	 */
	void updateBindStatus(int orderId) throws Exception;
	
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
	 * @return
     */
	List<PaymentLimitVO> getLimitList(String bankCode, String channelCode);
}
