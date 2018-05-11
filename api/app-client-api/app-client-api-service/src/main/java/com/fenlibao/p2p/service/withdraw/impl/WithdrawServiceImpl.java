package com.fenlibao.p2p.service.withdraw.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.trade.WithdrawDao;
import com.fenlibao.p2p.model.entity.pay.BranchInfo;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.withdraw.IWithdrawService;
import com.fenlibao.p2p.util.loader.Payment;

@Service
public class WithdrawServiceImpl extends BaseAbstractService implements IWithdrawService{

	@Resource
	private WithdrawDao withdrawDao;


	@Override
	public int saveBranchInfo(BranchInfo info) throws Exception {
		return this.withdrawDao.saveBranchInfo(info);
	}

	@Override
	public BranchInfo getBranchInfoByOrderId(int orderId) {
		return this.withdrawDao.getBranchInfoByOrderId(orderId);
	}

	@Override
	public BigDecimal getPoundage(Integer userId) {
		String amount = Payment.get(Payment.WITHDRAW_POUNDAGE_1_RMB);
		BigDecimal poundage = new BigDecimal(amount);
		Integer successApplyId = withdrawDao.getSuccessApplyId(userId);
		if (successApplyId == null || successApplyId < 1) {
			poundage = BigDecimal.ZERO;
		}
		return poundage;
	}

	@Override
	public BigDecimal getLimitAmount(int userId) {
		return withdrawDao.getLimitAmount(userId);
	}


}
