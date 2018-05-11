package com.fenlibao.p2p.service.withdraw;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6101_F03;


// copy from com.dimeng.p2p.account.user.service.TxManage
public interface IWithdrawManageService {

	/**
	 * <dt>
	 * <dl>
	 * 提现
	 * </dl>
	 * <dl>
	 * <ol>
	 * <li>判断参数funds>=0,withdrawPsd不为空串或null</li>
	 * <li>插入表T6034</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param funds
	 *            提现金额
	 * @param withdrawPsd
	 *            交易密码
	 * @param cardNumber
	 *            银行卡号
	 * @throws ParameterException
	 *             银行卡号有误
	 * @throws LogicalException
	 *             交易密码错误
	 * @throws SQLException
	 *             操作数据库异常
	 */
	public abstract int withdraw(BigDecimal funds, String withdrawPsd,
			int cardId, T6101_F03 f03,boolean txkcfs, int userId) throws Throwable;

	/**
	 * 检查交易密码是否正确
	 * @param withdrawPsd
	 * @return
	 * @throws Throwable
	 */
    public abstract boolean checkWithdrawPassword(String withdrawPsd, int userId) throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 添加银行卡
	 * </dl>
	 * <dl>
	 * <ol>
	 * <li>判断参数均不能为空，银行卡有特殊规则</li>
	 * <li>插入T6024表</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param bank
	 *            开户银行
	 * @param bankAddr
	 *            开户银行地址
	 * @param cardNumber
	 *            银行卡号
	 * @param branchBank
	 *            开户支行
	 * @throws ParameterException
	 *             银行卡号错误
	 * @throws SQLException
	 *             操作数据库异常
	 */
	public abstract void addBankCard(String bank, String bankAddr,
			String branchBank, String cardNumber) throws Throwable;

	/**
	 * 获取身份证和手机的认证状态
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean getVerifyStatusItem(int userId) throws Throwable;

	public abstract int withdrawHdw(BigDecimal funds, String withdrawPsd,
			int cardId, T6101_F03 f03, int userId) throws Throwable;
	
}
