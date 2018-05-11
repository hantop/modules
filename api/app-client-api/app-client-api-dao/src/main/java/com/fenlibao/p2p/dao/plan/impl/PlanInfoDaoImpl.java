package com.fenlibao.p2p.dao.plan.impl;

import com.fenlibao.p2p.dao.plan.PlanInfoDao;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanTotalRepay;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlanInfoDaoImpl implements PlanInfoDao{

	@Resource
	private SqlSession sqlSession;

	private static final String MAPPER = "PlanInfoMapper.";

	@Override
	public List<Integer> findPlanIds(int lessThanSurplusAmount, Date nowTime, int status) {
		Map<Object, Object> map = new HashMap<>();
		map.put("lessThanSurplusAmount", lessThanSurplusAmount);
		map.put("nowTime", nowTime);
		map.put("status", status);
		return sqlSession.selectList(MAPPER + "findPlanIds", map);
	}

	@Override
	public InvestPlanInfo getPlanInfo(int id) {
		return sqlSession.selectOne(MAPPER+"getPlanInfoById",id);
	}

	@Override
	public List<UserPlan> getUserPlansByPlanId(int planId) {
		return sqlSession.selectList(MAPPER+"getUserPlansByPlanId",planId);
	}

	@Override
	public UserCouponInfo getUserCoupon(int userPlanId) {
		return sqlSession.selectOne(MAPPER+"getUserCoupon",userPlanId);
	}

	@Override
	public void batchInsertUserPlanRepayPlan(List<UserPlanTotalRepay> list) {
		sqlSession.insert(MAPPER+"batchInsertUserPlanRepayPlan",list);
	}

    @Override
    public void updatePlanTermination(int planId, Date expireTime, Date bearrateDate, int status, Date fullTime, Date updateTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", planId);
		map.put("expireTime", expireTime);
		map.put("fullTime", fullTime);
		map.put("updateTime", updateTime);
		map.put("bearrateDate", bearrateDate);
		map.put("status", status);
		sqlSession.update(MAPPER + "updatePlanTermination", map);
    }

	@Override
	public void insertPlanTermination(int planId, int operatorId) {
		Map<String, Object> map = new HashMap<>();
		map.put("planId", planId);
		map.put("operatorId", operatorId);
		sqlSession.insert(MAPPER + "insertPlanTermination", map);
	}
	@Override
	public List<Integer>  getPlanInfoNotFull(HashMap map){
		return sqlSession.selectList(MAPPER+"getPlanInfoNotFull",map);
	}



}
