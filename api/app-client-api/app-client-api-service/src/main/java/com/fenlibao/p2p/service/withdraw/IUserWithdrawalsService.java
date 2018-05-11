package com.fenlibao.p2p.service.withdraw;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import com.fenlibao.p2p.model.entity.pay.Bank;
import com.fenlibao.p2p.model.entity.pay.UserWithdrawals;
import com.fenlibao.p2p.model.form.trade.WithdrawManageFrom;

/**
 * 用户提现管理
 * <p>copy from com.dimeng.p2p.modules.account.console.service.UserWithdrawalsManage
 * @author yangzengcai
 * @date 2015年8月31日
 */
public interface IUserWithdrawalsService {

	/**
	 * <dt>
	 * <dl>
	 * 描述： 提现统计记录
	 * </dl>
	 * </dt>
	 * 
	 * @return Extraction 提现实体对象
	 * @throws Throwable
	 */
	public WithdrawManageFrom getExtractionInfo() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 批量审核提现记录
	 * </dl>
	 * </dt>
	 * 
	 * @param passed
	 *            是否放款
	 * @param check_reason
	 *            审核不通过意见
	 * @param accountId
	 *            操作人ID
	 * @param ids
	 *            提现申请ID列表
	 * @throws Throwable
	 */
	public void check(boolean passed, String check_reason, int accountId, int... ids)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 批量放款
	 * </dl>
	 * </dt>
	 * 
	 * @param passed
	 *            是否放款
	 * @param check_reason
	 *            拒绝放款意见
	 * @param accountId
	 *            操作人ID
	 * @param ids
	 *            提现申请ID列表
	 * @return orderIds 放款通过生成的订单ID列表
	 * @throws Throwable
	 */
	public int[] fk(boolean passed, String check_reason, int accountId, int... ids)
			throws Throwable;

	/**
	 * 查询所有银行卡
	 * 
	 * @return
	 * @throws Throwable
	 */
	public Bank[] getBanks() throws Throwable;

	/**
	 * 导入提现
	 * 
	 * @param inputStream
	 * @param real_name
	 * @param charset
	 * @throws Throwable
	 */
	public void importData(InputStream inputStream, String real_name,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出提现审核通过
	 * </dl>
	 * </dt>
	 * 
	 * @param txglRecord
	 *            审核通过记录
	 * @throws Throwable
	 */
	public void export(UserWithdrawals[] txglRecord, OutputStream outputStream,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出已提现
	 * </dl>
	 * </dt>
	 * 
	 * @param txglRecord
	 *            审核通过记录
	 * @throws Throwable
	 */
	public void exportYtx(UserWithdrawals[] txglRecord,
			OutputStream outputStream, String charset) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出已提现。这个方法跟上面那个是exportYtx是重载方法。
	 * </dl>
	 * </dt>
	 * 
	 * @param txglRecord
	 *            审核通过记录
	 * @throws Throwable
	 */
	public void exportYtxContent(UserWithdrawals[] txglRecord,
			OutputStream outputStream, String charset) throws Throwable;

	/**
	 * 提现成功总额
	 */
	public abstract BigDecimal getTxze() throws Throwable;

	/**
	 * 提现成功手续费
	 */
	public abstract BigDecimal getTxsxf() throws Throwable;
	
}
