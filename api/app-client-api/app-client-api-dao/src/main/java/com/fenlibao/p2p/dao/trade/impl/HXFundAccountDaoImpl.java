package com.fenlibao.p2p.dao.trade.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.HXFundAccountDao;
import org.springframework.stereotype.Repository;

@Repository
public class HXFundAccountDaoImpl extends BaseDao implements HXFundAccountDao {

	public HXFundAccountDaoImpl() {
		super("HXFundAccountMapper");
	}

	@Override
	public int queryFundAccountExist(int userId) {
		return sqlSession.selectOne(MAPPER+"queryFundAccountExist",userId);
	}

	@Override
	public int queryHXAccountExist(int userId) {
		return sqlSession.selectOne(MAPPER+"queryHXAccountExist",userId);
	}

	@Override
	public int
	queryActive(int userId) {
		return sqlSession.selectOne(MAPPER+"queryActive",userId);
	}

	@Override
	public String queryEAccountNo(int userId) {
		return sqlSession.selectOne(MAPPER+"queryEAccountNo",userId);
	}
}
