package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.UserAccount;

import java.math.BigDecimal;
import java.util.Map;

public interface UserAccountDao {

	public UserAccount getUserAccount(Map map);

	/**
	 * 新增资金账户
	 * @param map
	 * @return
	 */
	int addUserAccount(Map<String, Object> map);
	
	/**
	 * 获取用户往来账号余额
	 * @param userId
	 * @return
	 */
	BigDecimal getWLZHBalance(Integer userId);

	/**
	 * 获取存管余额
	 * @param userId
	 * @return
	 */
    BigDecimal getCgBalance(Integer userId);

	/**
	 * 获取账户冻结金额
	 * @param userId
	 * @return
	 */
	BigDecimal getSdUserFreezeSum(Integer userId);

	/**
	 * 获取账户冻结金额
	 * @param userId
	 * @return
	 */
	BigDecimal getNewTenderFreezeSum(Integer userId,int cgNum);

	/**
	 * 获取计划投资的冻结金额
	 * @param userId
	 * @return
	 */
	BigDecimal getPlanFreezeSum(Integer userId);
}
