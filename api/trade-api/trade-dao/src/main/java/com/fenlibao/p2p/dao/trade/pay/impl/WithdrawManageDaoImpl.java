package com.fenlibao.p2p.dao.trade.pay.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fenlibao.p2p.model.trade.vo.WithdrawDelayRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.trade.pay.WithdrawManageDao;
import com.fenlibao.p2p.model.trade.entity.pay.T6130;

@Repository
public class WithdrawManageDaoImpl implements WithdrawManageDao{

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "WithdrawManageMapper.";
	
	@Override
	public Integer getSuccessApplyId(Integer userId) {
		return sqlSession.selectOne(MAPPER + "getSuccessApplyId", userId);
	}
	
	@Override
	public BigDecimal getOfflineRechargeAmount(int userId, int hours) {
		Map<String, Integer> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("hours", hours);
		return sqlSession.selectOne(MAPPER + "getOfflineRechargeAmount", params);
	}

	@Override
	public void addWithdrawApply(T6130 t6130) {
		sqlSession.insert(MAPPER + "addWithdrawApply", t6130);
	}

	@Override
	public void updateWithdrawApply(T6130 t6130) {
		sqlSession.insert(MAPPER + "updateWithdrawApply", t6130);
	}

	@Override
	public void insertWithdrawDelayRequest(Integer userId, BigDecimal withdrawAmount, String userInfo, String bankCard, String flowNum, Integer orderId) {
		Map<String, Object> m = new HashMap<>();
		m.put("userId", userId);
		m.put("withdrawAmount", withdrawAmount);
		m.put("userInfo", userInfo);
		m.put("bankCard", bankCard);
		m.put("flowNum", flowNum);
		m.put("orderId", orderId);
		sqlSession.insert(MAPPER + "insertWithdrawDelayRequest", m);
	}

	@Override
	public List<WithdrawDelayRequest> findWithdrawDelayRequests(Integer limit) {
		Map<String,Object> m = new HashMap<>();
		m.put("limit", limit);
		return sqlSession.selectList(MAPPER + "findWithdrawDelayRequests", m);
	}

	@Override
	public void updateWithdrawDelayRequestsWithdrawState(Integer id) {
		sqlSession.update(MAPPER + "updateWithdrawDelayRequestsWithdrawState", id);
	}

	@Override
	public Integer getTransactionState(String withdrawDelayRequestJob) {
		return sqlSession.selectOne(MAPPER + "getTransactionState", withdrawDelayRequestJob);
	}

}
