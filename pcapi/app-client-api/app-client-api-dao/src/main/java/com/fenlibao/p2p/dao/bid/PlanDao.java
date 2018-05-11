package com.fenlibao.p2p.dao.bid;

import com.fenlibao.p2p.model.entity.bid.DirectionalPlan;
import com.fenlibao.p2p.model.entity.borrow.BorrowerEntity;
import com.fenlibao.p2p.model.entity.finacing.InvestInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanInvestInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


public interface PlanDao {

    /**
     * 扣除计划可投金额
     *
     * @return
     */
    int updateSurplusAmountForInvestPlan(int planId, BigDecimal amount);

    /**
     * 增加投资记录
     *
     * @return
     */
    int insertRecordForInvestPlan(int userId, int planId, BigDecimal amount, BigDecimal freezeAmount);

    /**
     * 更新用户的退出状态
     * @param userId
     * @param userPlanId
     * @param timestamp
     */
    void updatePlanQuitStatus(int userId, int userPlanId, Timestamp timestamp);

    /**
     * 插入用户退出的信息
     * @param userPlanId
     * @param timestamp
     */
    void insertExitRecord(int userPlanId, Timestamp timestamp);

    /**
     * 锁用户投资计划记录
     * @param userPlanId
     */
    void lockUserPlan(int userPlanId);
    /**
     * 获取产品的定向条件
     * @param productId
     * @return
     */
    DirectionalPlan getDirectionalPlan(int productId);

    /**
     * 获取用户的计划退出的信息
     * @param planRecordId
     * @param userId
     * @return
     */
    PlanRecordInfo getPlanRecordInfo(int planRecordId, int userId);

    /**
     * 获取产品库中的产品信息
     * @param userPlanId
     * @return
     */
    List<UserPlanProduct> getUserPlanProducts(int userPlanId);

    /**
     * 获取最近投资的新计划列表
     *
     * @param userId
     * @param num
     * @param pageBounds
     * @return
     */
    List<InvestInfo> getNewPlanList(int userId, int num, VersionTypeEnum vte, PageBounds pageBounds);

    /**
     * 获取最近投资的老计划列表
     * @param userId
     * @param num
     * @param pageBounds
     * @return
     */
    List<InvestInfo> getOldPlanList(int userId, int num, PageBounds pageBounds);

    /**
     * 获取我的持有计划
     * @param userId
     * @param pageBounds
     * @return
     */
    List<PlanInvestInfo> getAllMyPlan(Integer userId, VersionTypeEnum vte, PageBounds pageBounds);

    /**
     * 获取我的退出中的计划
     * @param userId
     * @param pageBounds
     * @return
     */
    List<PlanInvestInfo> getMyQuitPlans(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds);

    /**
     * 获取我的成功退出的计划
     * @param userId
     * @param pageBounds
     * @return
     */
    List<PlanInvestInfo> getMySuccessPlans(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds);
    /**
     * 获取用户退出中的计划
     * @param userPlanId
     * @return
     */
    UserPlan getExitPlan(int userPlanId);

    List<BorrowerEntity> getPlanBidList(int planId,PageBounds pageBounds);
}
