package com.fenlibao.p2p.dao.funds.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.fenlibao.p2p.model.entity.funds.FundsFlowEntity;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.funds.IFundsDao;
import com.fenlibao.p2p.model.entity.UserAccount;

@Repository
public class FundsDaoImpl extends BaseDao implements IFundsDao {
	
	public FundsDaoImpl() {
		super("FundsMapper");
	}

	@Override
	public UserAccount lockAccount(int userId, String accountType) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("accountType", accountType);
		return sqlSession.selectOne(MAPPER + "lockAccount", params);
	}

	@Override
	public Integer getAccountId(String account, String accountType) {
		Map<String, Object> params = new HashMap<>();
		params.put("account", account);
		params.put("accountType", accountType);
		return sqlSession.selectOne(MAPPER + "getAccountId", params);
	}

	@Override
	public UserAccount lockAccountById(int accountId) {
		return sqlSession.selectOne(MAPPER + "lockAccountById", accountId);
	}

	@Override
	public int insertFundsflowRecord(int fundsAccountId, int relativelyAccountId, int tradeTypeId, BigDecimal income, BigDecimal expenditure,
			BigDecimal balance, String remark) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("fundsAccountId", fundsAccountId);
		params.put("relativelyAccountId", relativelyAccountId);
		params.put("tradeTypeId", tradeTypeId);
		params.put("income", income);
		params.put("expenditure", expenditure);
		params.put("balance", balance);
		params.put("remark", remark);
		sqlSession.insert(MAPPER + "insertFundsflowRecord", params);
		if(params.get("id")!=null){
			return Integer.valueOf(String.valueOf(params.get("id")));
		}
		return 0;
	}

	@Override
	public void insertFundsflowRecord(FundsFlowEntity fundsFlowEntity) throws Exception {
		sqlSession.update(MAPPER + "insertFundsflowRecord2", fundsFlowEntity);
	}

	@Override
	public void increaseOrSubtractAccountBalance(int accountId, BigDecimal amount) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("accountId", accountId);
		params.put("amount", amount);
		sqlSession.update(MAPPER + "increaseOrSubtractAccountBalance", params);
	}

	@Override
	public Map<String,Object> getPlatformAccountId(String accountType) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("accountType", accountType);
		return sqlSession.selectOne(MAPPER + "getPlatformAccountId", params);
	}

}
