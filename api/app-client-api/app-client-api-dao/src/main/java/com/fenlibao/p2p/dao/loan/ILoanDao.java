package com.fenlibao.p2p.dao.loan;

import com.fenlibao.p2p.model.form.loan.LoanApplicationForm;

public interface ILoanDao {

	/**
	 * 添加借款申请
	 * @param loan
	 * @throws Exception
	 */
	void add(LoanApplicationForm loan) throws Exception;
	
	/**
	 * 获取未处理的借款申请数量
	 * @return
	 */
	Integer getUntreatedQty(String phoneNum);
	
	/**
	 * 获取最新借款编码
	 * @return
	 */
	String getNewestCode();
	
	/**
	 * 获取真实姓名
	 * @param userId
	 * @return
	 */
	String getRealName(String userId);
}
