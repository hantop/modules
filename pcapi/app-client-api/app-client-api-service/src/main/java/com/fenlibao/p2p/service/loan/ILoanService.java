package com.fenlibao.p2p.service.loan;

import com.fenlibao.p2p.model.form.loan.LoanApplicationForm;

public interface ILoanService {

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
	Integer getUntreatedQty(String userId);
}
