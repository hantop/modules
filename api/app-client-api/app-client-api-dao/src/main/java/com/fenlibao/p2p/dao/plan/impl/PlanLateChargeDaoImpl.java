package com.fenlibao.p2p.dao.plan.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.plan.PlanLateChargeDao;
import com.fenlibao.p2p.model.entity.plan.UserPlanRepayment;

@Repository
public class PlanLateChargeDaoImpl implements PlanLateChargeDao{

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "PlanLateChargeMapper.";
	
	@Override
	public List<UserPlanRepayment> getOverdueDebts() {
		return sqlSession.selectList(MAPPER+"getOverdueDebts");
	}

	@Override
	public UserPlanRepayment getUserPlanRepayment(Integer userPlanProductId, Integer tradeType) {
		Map<String,Object> param=new HashMap<>();
		param.put("userPlanProductId", userPlanProductId);
		param.put("tradeType", tradeType);
		return sqlSession.selectOne(MAPPER+"getUserPlanRepayment",param);
	}

	@Override
	public void insertUserPlanRepayment(UserPlanRepayment userPlanRepayment) {
		sqlSession.insert(MAPPER+"insertUserPlanRepayment", userPlanRepayment);
	}

	@Override
	public void updateUserPlanRepayment(Map<String, Object> param) {
		sqlSession.update(MAPPER+"updateUserPlanRepayment", param);
	}

	@Override
	public void batchInsertUserPlanRepayment(List<UserPlanRepayment> list) {
		sqlSession.insert(MAPPER+"batchInsertUserPlanRepayment", list);
	}

}
