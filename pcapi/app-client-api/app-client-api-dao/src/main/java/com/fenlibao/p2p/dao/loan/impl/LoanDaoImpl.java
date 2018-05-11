package com.fenlibao.p2p.dao.loan.impl;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.loan.ILoanDao;
import com.fenlibao.p2p.model.form.loan.LoanApplicationForm;

@Repository
public class LoanDaoImpl extends BaseDao implements ILoanDao {

	public LoanDaoImpl() {
		super("LoanMapper");
	}
	
	@Override
	public void add(LoanApplicationForm loan) throws Exception {
		sqlSession.insert(MAPPER + "add", loan);
	}

	@Override
	public Integer getUntreatedQty(String userId) {
		return sqlSession.selectOne(MAPPER + "getUntreatedQty",  userId);
	}

	@Override
	public String getNewestCode() {
		return sqlSession.selectOne(MAPPER + "getNewestCode");
	}

	@Override
	public String getRealName(String userId) {
		return sqlSession.selectOne(MAPPER + "getRealName", userId);
	}

}
