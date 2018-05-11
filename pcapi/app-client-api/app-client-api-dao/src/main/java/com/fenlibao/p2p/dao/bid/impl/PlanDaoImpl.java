package com.fenlibao.p2p.dao.bid.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.model.entity.bid.DirectionalPlan;
import com.fenlibao.p2p.model.entity.bid.InvestUserPlan;
import com.fenlibao.p2p.model.entity.borrow.BorrowerEntity;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanInvestInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

    /**
     * 扣除计划可投金额
     *
     * @return
     */
    @Override
    public int updateSurplusAmountForInvestPlan(int planId, BigDecimal amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("planId", planId);
        map.put("amount", amount);
        return sqlSession.update(MAPPER + "updateSurplusAmountForInvestPlan", map);
    }

    /**
     * 增加投资记录
     *
     * @return
     */
    @Override
    public int insertRecordForInvestPlan(int userId, int planId, BigDecimal amount, BigDecimal freezeAmount) {
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
    public DirectionalPlan getDirectionalPlan(int productId) {
        return sqlSession.selectOne(MAPPER + "getDirectionById", productId);
    }

    @Override
    public PlanRecordInfo getPlanRecordInfo(int planRecordId, int userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("planRecordId", planRecordId);
        param.put("userId", userId);
        return sqlSession.selectOne(MAPPER+"getPlanRecordInfo",param);
    }

    @Override
    public List<UserPlanProduct> getUserPlanProducts(int userPlanId) {
        return sqlSession.selectList(MAPPER + "getUserPlanProducts", userPlanId);
    }

    @Override
    public List<InvestInfo> getNewPlanList(int userId, int num, VersionTypeEnum vte, PageBounds pageBounds) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("num", num);
        param.put("versionType", vte == null ? VersionTypeEnum.PT.getIndex() : vte.getIndex());
        return sqlSession.selectList(MAPPER + "getNewPlanList", param,pageBounds);
    }

    @Override
    public List<InvestInfo> getOldPlanList(int userId, int num, PageBounds pageBounds) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("num", num);
        return sqlSession.selectList(MAPPER + "getOldPlanList", param,pageBounds);
    }

    @Override
    public List<PlanInvestInfo> getAllMyPlan(Integer userId, VersionTypeEnum vte, PageBounds pageBounds) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("versionType", vte == null ? VersionTypeEnum.PT.getIndex() : vte.getIndex());
        return sqlSession.selectList(MAPPER + "getAllMyPlan", param, pageBounds);
    }

    @Override
    public List<PlanInvestInfo> getMyQuitPlans(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
        return sqlSession.selectList(MAPPER + "getMyQuitPlans", param, pageBounds);
    }

    @Override
    public List<PlanInvestInfo> getMySuccessPlans(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
        return sqlSession.selectList(MAPPER + "getMySuccessPlans", param,pageBounds);
    }

    @Override
    public UserPlan getExitPlan(int userPlanId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userPlanId", userPlanId);
        return sqlSession.selectOne("getUserQuitPlan",params);
    }

    @Override
    public List<BorrowerEntity> getPlanBidList(int planId, PageBounds pageBounds) {
        Map<String, Object> param = new HashMap<>();
        param.put("planId", planId);
        return sqlSession.selectList(MAPPER + "getPlanBidList", param,pageBounds);
    }
}
