package com.fenlibao.p2p.service.funds;

import java.math.BigDecimal;

import com.fenlibao.p2p.model.entity.FeeType;
import com.fenlibao.p2p.model.entity.UserAccount;


/**
 * 资金
 * @author yangzengcai
 * @date 2015年11月20日
 */
public interface IFundsService {

	/**
	 * 转账（平台给用户转账）
	 * <p>使用的时候注意事务的把控，调用该方法的service必须要有事务，否侧抛异常！！！</p>
	 * @param userId 收款用户ID
	 * @param amount 转账金额
	 * @param feeType 交易类型
	 * @param accountType 账号类型（WLZH,LDZH...）
	 * @param platformAccount 平台转账账号
	 * @throws Exception
	 */
	void transfer(int userId, BigDecimal amount, FeeType feeType, 
			String accountType, String platformAccount) throws Exception;
	
	/**
	 * 获取用户账号信息并锁定(T6101)
	 * @param userId
	 * @param accountType
	 * @return
	 */
	UserAccount lockAccount(int userId, String accountType);
	
	/**
	 * 根据账号和账号类型获取账号ID(T6101)
	 * @param account
	 * @param accountType
	 * @return
	 */
	Integer getAccountId(String account, String accountType);
	
	/**
	 * 根据账号ID锁定资金账号
	 * @param id
	 * @return
	 */
	UserAccount lockAccountById(int accountId);
	
	/**
	 * 插入资金流水记录
	 * @param turninAccountId 资金账号ID
	 * @param turnoutAccountId 对方资金账号ID
	 * @param tradeTypeId 交易类型ID
	 * @param amount 转账金额
	 * @param balance 账户余额
	 * @param remark 备注
	 * @throws Exception
	 */
	void insertFundsflowRecord(int fundsAccountId, int relativelyAccountId, int tradeTypeId, BigDecimal income, 
			BigDecimal expenditure, BigDecimal balance, String remark) throws Exception;
	
	/**
	 * 增加/减少账户余额
	 * @param accountId (t6101.F01)
	 * @param amount 金额（正为增加，负为减少）
	 * @throws Exception
	 */
	void increaseOrSubtractAccountBalance(int accountId, BigDecimal amount) throws Exception;
}
