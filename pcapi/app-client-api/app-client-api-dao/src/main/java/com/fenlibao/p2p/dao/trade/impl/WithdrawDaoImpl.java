package com.fenlibao.p2p.dao.trade.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.WithdrawDao;
import com.fenlibao.p2p.model.entity.pay.BranchInfo;

@Repository
public class WithdrawDaoImpl extends BaseDao implements WithdrawDao {
	
	public WithdrawDaoImpl() {
		super("WithdrawMapper");
	}

	@Override
	public BigDecimal getWithdrawFreezeSum(String userId) {
		return sqlSession.selectOne(MAPPER + "getWithdrawFreezeSum", userId);
	}

	@Override
	public BigDecimal getWithdrawFreezeSumByDepository(String userId) {
		return sqlSession.selectOne(MAPPER + "getWithdrawFreezeSumByDepository", userId);
	}

	@Override
	public int saveBranchInfo(BranchInfo info) throws Exception {
		return sqlSession.insert(MAPPER + "saveBranchInfo", info);
	}

	@Override
	public BranchInfo getBranchInfoByOrderId(int orderId) {
		return sqlSession.selectOne(MAPPER + "getBranchInfo", orderId);
	}

	@Override
	public Integer getSuccessApplyId(Integer userId) {
		return sqlSession.selectOne(MAPPER + "getSuccessApplyId", userId);
	}

	@Override
	public BigDecimal getLimitAmount(int userId) {
		return sqlSession.selectOne(MAPPER + "getLimitAmount", userId);
	}

	@Override
	public BigDecimal getCGWithdrawFreezeSum(Integer userId,String cgMode) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("cgMode", cgMode);
		return sqlSession.selectOne(MAPPER + "getCGWithdrawFreezeSum", params);
	}
}
