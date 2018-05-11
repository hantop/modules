package com.fenlibao.p2p.dao.trade.pay;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.trade.entity.pay.T6130;
import com.fenlibao.p2p.model.trade.vo.WithdrawDelayRequest;

public interface WithdrawManageDao {
	/**
	 * 只获取一个提现申请成功的提现申请订单,用于判断是否需要有续费
	 * @param userId
	 * @return
	 */
	Integer getSuccessApplyId(Integer userId);
	
	/**
	 * 获取某个用户 n小时内 线下充值总额
	 * @param userId
	 * @param hours
	 * @return
	 */
	BigDecimal getOfflineRechargeAmount(int userId, int hours);
	
	/**
	 * 新建提现申请
	 */
	void addWithdrawApply(T6130 t6130);
	
	void updateWithdrawApply(T6130 t6130);

	/**
	 * 提现延迟请求第三方支付记录表
	 * @param userId
	 * @param withdrawAmount
	 * @param userInfo
	 * @param bankCard
	 * @param flowNum
	 */
	void insertWithdrawDelayRequest(Integer userId, BigDecimal withdrawAmount, String userInfo, String bankCard, String flowNum, Integer orderId);

	/**
	 * 取出提现延迟请求第三方支付记录表
	 * @param limit
	 * @return
	 */
	List<WithdrawDelayRequest> findWithdrawDelayRequests(Integer limit);

	/**
	 * 更新提现延迟请求第三方支付记录表该记录为已处理
	 * @param id
	 */
	void updateWithdrawDelayRequestsWithdrawState(Integer id);

	/**
	 * 获取事务状态
	 * @param withdrawDelayRequestJob
	 * @return
	 */
	Integer getTransactionState(String withdrawDelayRequestJob);
}
