package com.fenlibao.p2p.service.loan.impl;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.loan.ILoanDao;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.form.loan.LoanApplicationForm;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.loan.ILoanService;
import com.fenlibao.p2p.util.loan.LoanUtil;

@Service
public class LoanServiceImpl implements ILoanService {

	@Resource
	private ILoanDao loanDao;
	
	@Override
	public void add(LoanApplicationForm loan) throws Exception {
		String code = loanDao.getNewestCode();
		code = LoanUtil.generateCode(code);
		if (LoanUtil.END.equals(code)) {
			throw new BusinessException(ResponseCode.LOAN_FREQUENCY_OVER.getCode(),
					ResponseCode.LOAN_FREQUENCY_OVER.getMessage());
		}
		String realName = loan.getContacts();
		if (realName.indexOf("*") != -1) {
			realName = loanDao.getRealName(loan.getUserId());
			if (StringUtils.isNotBlank(realName)) {
				loan.setContacts(realName);
			}
		}
		loan.setCode(code);
		loanDao.add(loan);
	}

	@Override
	public Integer getUntreatedQty(String userId) {
		return loanDao.getUntreatedQty(userId);
	}

}
