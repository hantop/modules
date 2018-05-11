package com.fenlibao.p2p.service.recharge;

import java.math.BigDecimal;

import com.fenlibao.p2p.model.entity.pay.RechargeOrder;

public interface IRechargeMangeService {

	/**
	 * 添加充值订单
	 * 
	 * @param amount
	 *            充值金额
	 * @param payCompanyCode
	 *            支付公司代号
	 * @return
	 * 
	 * @throws Throwable
	 */
	public abstract RechargeOrder addOrder(BigDecimal amount, int payCompanyCode, int userId, boolean isBind)
			throws Throwable;
	
	/**
	 * 获取充值订单
	 * @param orderId
	 * @return
	 * @throws Throwable
	 */
	public abstract RechargeOrder getChargeOrder(int orderId) throws Throwable;
	
	/**
	 * 获取银行卡号
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public String getBankCard(int id) throws Throwable;
	
}
