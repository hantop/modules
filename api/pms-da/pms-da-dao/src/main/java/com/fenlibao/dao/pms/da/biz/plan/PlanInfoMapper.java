package com.fenlibao.dao.pms.da.biz.plan;

import com.fenlibao.model.pms.da.biz.plan.InvestPlanInfo;
import com.fenlibao.model.pms.da.biz.plan.UserCouponInfo;
import com.fenlibao.model.pms.da.biz.plan.UserPlan;
import com.fenlibao.model.pms.da.biz.plan.UserPlanTotalRepay;

import java.util.List;

public interface PlanInfoMapper {

	InvestPlanInfo getPlanInfoById(int planId);

	List<UserPlan> getUserPlansByPlanId(int planId);

	UserCouponInfo getUserCoupon(Integer userPlanId);

	void batchInsertUserPlanRepayPlan(List<UserPlanTotalRepay> listToInsert);
}
