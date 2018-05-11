package com.fenlibao.p2p.dao.bid.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.model.entity.bid.DirectionalPlan;
import com.fenlibao.p2p.model.entity.bid.InvestUserPlan;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacingInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zcai on 2017/2/16.
 */
@Repository
public class PlanDaoImpl extends BaseDao implements PlanDao {

    public PlanDaoImpl() {
        super("PlanMapper");
    }

    @Override
    public List<AutoTenderVO> getPlanIdList_TBZ() {
        return sqlSession.selectList(MAPPER + "getPlanIdList_TBZ");
    }

    @Override
    public List<AutoTenderVO> getBidListByPlanId(int planId) {
        return sqlSession.selectList(MAPPER + "getBidListByPlanId", planId);
    }

    @Override
    public int updatePlanState(int planId) {
        return sqlSession.update(MAPPER + "updatePlanState", planId);
    }

    @Override
    public Integer getPlanIdByBidId(int bidId) {
        return sqlSession.selectOne(MAPPER + "getPlanIdByBidId", bidId);
    }

    @Override
    public DirectionalPlan getDirectionalPlan(int productId) {
        return sqlSession.selectOne(MAPPER + "getDirectionById", productId);
    }

    @Override
    public PlanRecordInfo getPlanRecordInfo(int planRecordId, Integer userId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("planRecordId", planRecordId);
        map.put("userId", userId);
        return sqlSession.selectOne(MAPPER+"getPlanRecordInfo",map);
    }

    @Override
    public List<UserPlanProduct> getUserPlanProducts(int userPlanId) {
        return sqlSession.selectList(MAPPER + "getUserPlanProducts", userPlanId);
    }

    /**
     * 扣除计划可投金额
     * @return
     */
    @Override
    public int updateSurplusAmountForInvestPlan(int planId, BigDecimal amount){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("planId", planId);
        map.put("amount", amount);
        return sqlSession.update(MAPPER + "updateSurplusAmountForInvestPlan", map);
    }

    /**
     * 增加投资记录
     * @return
     */
    @Override
    public int insertRecordForInvestPlan(int userId, int planId, BigDecimal amount, BigDecimal freezeAmount){
        InvestUserPlan investUserPlan = new InvestUserPlan();
        investUserPlan.setUserId(userId);
        investUserPlan.setPlanId(planId);
        investUserPlan.setInvestAmount(amount);
        investUserPlan.setFreezeAmount(freezeAmount);
        sqlSession.update(MAPPER + "insertRecordForInvestPlan", investUserPlan);
        return investUserPlan.getId();
    }


    @Override
    public void updatePlanQuitStatus(int userId, int userPlanId, Timestamp timestamp) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("userPlanId", userPlanId);
        param.put("timestamp", timestamp);

        sqlSession.update(MAPPER + "updatePlanQuitStatus", param);
    }

    @Override
    public void insertExitRecord(int userPlanId, Timestamp timestamp) {
        Map<String, Object> param = new HashMap<>();
        param.put("userPlanId", userPlanId);
        param.put("timestamp", timestamp);

        sqlSession.insert(MAPPER + "insertExitRecord", param);
    }

    @Override
    public void lockUserPlan(int userPlanId) {

        sqlSession.insert(MAPPER + "lockUserPlan", userPlanId);
    }

    @Override
    public List<PlanFinacingInfo> getHoldPlan(int userId, int isUp, Date time) {
        return getHoldPlan( userId,  isUp,  time, 1);
    }

    @Override
    public List<PlanFinacingInfo> getHoldPlan(int userId, int isUp, Date time, int versionTypeEnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("time", time);
        params.put("versionTypeEnum", versionTypeEnum);
        return sqlSession.selectList(MAPPER + "getHoldPlan", params);
    }

    @Override
    public List<PlanFinacingInfo> getQuitPlan(int userId, int isUp, Date time) {
        return getQuitPlan(userId,isUp,time,1);
    }

    @Override
    public List<PlanFinacingInfo> getQuitPlan(int userId, int isUp, Date time, int versionTypeEnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("time", time);
        params.put("versionTypeEnum", versionTypeEnum);
        return sqlSession.selectList(MAPPER + "getQuitPlan", params);
    }

    @Override
    public List<PlanFinacingInfo> getProfitPlan(int userId, int isUp, Date time) {
        return getProfitPlan(userId,  isUp,  time,1);
    }

    @Override
    public List<PlanFinacingInfo> getProfitPlan(int userId, int isUp, Date time, int versionTypeEnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("time", time); params.put("versionTypeEnum", versionTypeEnum);
        return sqlSession.selectList(MAPPER + "getProfitPlan", params);
    }

    @Override
    public UserPlan getExitPlan(int userPlanId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userPlanId", userPlanId);
        return sqlSession.selectOne("getUserQuitPlan",params);
    }

    @Override
    public Integer getVersionType(int planId) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("planId", planId);
        return sqlSession.selectOne(MAPPER + "getVersionType", params);
    }
}
