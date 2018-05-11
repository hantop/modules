package com.fenlibao.p2p.service.pay;

import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;

/**
 * 用户信息管理
 */
public interface IUserInfoManageService {

	/**
	 * 查询用户是否逾期
	 * @return
	 * @throws Throwable
	 */
	public abstract String isYuqi(int userId) throws Throwable;
	/**
	 * 是否实名认证
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isSmrz(int userId) throws Throwable;
	/**
	 * 是否设置交易密码
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isTxmm(int userId) throws Throwable;
	
	/**
	 * 查询用户资金信息
	 * @return
	 * @throws Throwable
	 */
	public abstract T6101 search(int userId)throws Throwable;

    /**
     * 查询用户风险备用金资金信息
     * @return
     * @throws Throwable
     */
    public abstract T6101 searchFxbyj(int userId)throws Throwable;
	/**
	 * 获取登陆用户姓名
	 * @param userID
	 * @return
	 * @throws Throwable
	 */
	public abstract String getUserName(int userID)throws Throwable;
	
	/**
	 * 查询用户基本信息
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6110 getUserInfo(int userId)throws Throwable;
	
	
}
