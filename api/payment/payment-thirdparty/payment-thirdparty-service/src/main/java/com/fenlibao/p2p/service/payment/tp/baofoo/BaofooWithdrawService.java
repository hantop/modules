package com.fenlibao.p2p.service.payment.tp.baofoo;

import java.math.BigDecimal;

public interface BaofooWithdrawService {
	/**
	 * 提现申请
	 * @param userId 用户id
	 * @param withdrawAmount 提现金额
	 * @param tradePassword 交易密码（解密）
	 * @throws Exception
	 */
    void withdrawApply(int userId,BigDecimal withdrawAmount, String tradePassword) throws Exception;
   
    /**
    * 提现结果查询
    * @param orderId 待确认的提现订单id
    * @throws Exception
    */
    void withdrawResultQuery(int orderId) throws Exception;

	/**
	 * 提现延迟请求第三方支付处理
	 */
	public void withdrawApplyAfter(Integer limit) throws Exception;

	/**
	 * 获取事务状态
	 * @param withdrawDelayRequestJob
	 * @return
	 */
	public Integer getTransactionState(String withdrawDelayRequestJob);
}
