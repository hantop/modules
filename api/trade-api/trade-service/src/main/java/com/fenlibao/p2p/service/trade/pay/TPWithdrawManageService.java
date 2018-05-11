package com.fenlibao.p2p.service.trade.pay;

import java.math.BigDecimal;

import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.model.user.vo.UserBankCardVO;

public interface TPWithdrawManageService {
	/**
	 * 提现申请
	 * @param userId 用户id
	 * @param totalAmount 金额总和
	 * @param withdrawAmount 提现金额
	 * @param poundage 手续费
	 * @param userInfo 用户信息
	 * @param bankCard 银行信息
	 * @param flowNum 流水号（用于t6501）
	 * @return
	 * @throws Exception
	 */
	int withdrawApply(int userId,BigDecimal totalAmount,BigDecimal withdrawAmount,BigDecimal poundage, UserInfoEntity userInfo,UserBankCardVO bankCard,String flowNum)throws Exception;
	
	/**
	 * 提现成功
	 * @param orderId 订单id（t6501.F01）
	 * @param thirdpartyOrderId 返回的第三方订单id
	 * @throws Exception
	 */
	void withdrawSuccess(int orderId,String thirdpartyOrderId)throws Exception;
	
	/**
	 * 提现失败
	 * @param orderId 订单id（t6501.F01）
	 * @param thirdpartyOrderId 返回的第三方订单id
	 * @throws Exception
	 */
	void withdrawFail(int orderId,String thirdpartyOrderId)throws Exception;
}
