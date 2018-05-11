package com.fenlibao.p2p.service.recharge.impl;

import com.fenlibao.p2p.dao.trade.HXFundAccountDao;
import com.fenlibao.p2p.service.base.abstracts.BaseAbstractService;
import com.fenlibao.p2p.service.recharge.HXAccountService;
import com.fenlibao.p2p.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HXAccountServiceImpl extends BaseAbstractService implements
		HXAccountService {

	@Resource
	private HXFundAccountDao hxFundAccountDao;

	@Override
	public int queryFundAccountExist(int userId) {
		return hxFundAccountDao.queryFundAccountExist(userId);
	}

	@Override
	public int queryHXAccountExist(int userId) {
		return hxFundAccountDao.queryHXAccountExist(userId);
	}

	@Override
	public int queryActive(int userId) {
		return hxFundAccountDao.queryActive(userId);
	}

	@Override
	public String queryEAccountNo(int userId) {
		String accountNo = null;
		String no = hxFundAccountDao.queryEAccountNo(userId);

		try {
			if (StringUtils.isNotBlank(no)) {
				accountNo = StringHelper.decode(no);
			}

		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return accountNo;
	}
}
