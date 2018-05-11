package com.fenlibao.p2p.dao.plan;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.plan.UserPlanRepayment;

public interface PlanLateChargeDao {
	List<UserPlanRepayment> getOverdueDebts();
	UserPlanRepayment getUserPlanRepayment(Integer userPlanProductId,Integer tradeType);
	void insertUserPlanRepayment(UserPlanRepayment userPlanRepayment);
	void updateUserPlanRepayment(Map<String,Object> param);
	void batchInsertUserPlanRepayment(List<UserPlanRepayment> list);
}
