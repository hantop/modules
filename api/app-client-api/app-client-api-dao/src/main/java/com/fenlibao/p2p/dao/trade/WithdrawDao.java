package com.fenlibao.p2p.dao.trade;

import com.fenlibao.p2p.model.entity.pay.BranchInfo;

import java.math.BigDecimal;

public interface WithdrawDao {
	/**
	 * 根据用户ID获取提现状态为待审核和待放款的总额。
	 * @param userId
	 * @return
	 */
	BigDecimal getWithdrawFreezeSum(String userId,String cgMode);
	
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
	 * 只获取一个提现申请成功的提现申请订单,用于判断是否需要有续费
	 * @param userId
	 * @return
	 */
	Integer getSuccessApplyId(Integer userId);

	/**
	 * 获取用提现限额
	 * @param userId
	 * @return
	 */
	BigDecimal getLimitAmount(int userId);

	/**
	 * 新网投资用户提现冻结资金
	 * @param platformNo
	 * @return
	 */
	BigDecimal getCGWithdrawFreezeSum(String platformNo);
}
