package com.fenlibao.p2p.service.withdraw;

import java.math.BigDecimal;

import com.fenlibao.p2p.model.entity.pay.BranchInfo;

public interface IWithdrawService {

	/**
	 * 保存支行信息
	 * @param info
	 * @throws Exception
	 */
	int saveBranchInfo(BranchInfo info) throws Exception;
	
	/**
	 * 根据当时提现的订单ID获取支行信息
	 * @param orderId
	 * @return
	 */
	BranchInfo getBranchInfoByOrderId(int orderId);
	
	/**
	 * 获取提现手续费
	 * @return
	 */
	BigDecimal getPoundage(Integer userId);

	/**
	 * 获取用提现限额
	 * @param userId
	 * @return
	 */
	BigDecimal getLimitAmount(int userId);
}
