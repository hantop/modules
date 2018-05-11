package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.finacing.PlanInvestInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface PlanService {
    /**
     * 终止
     * @param planId     计划ID
     * @param operatorId PMS用户ID
     *
     */
    void termination(int planId, int operatorId);

    /**
     * 终止人
     * @return
     */
    int getPmsUserId();

    /**
     * 投标计划
     *
     * @throws Throwable
     */
    void doPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable;

    /**
     * 投标计划
     *
     * @throws Throwable
     */
    void doPlan(Plan plan, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable;

    /**
     * 获取计划
     * @param planId
     * @return
     */
    Plan getPlan(int planId);

    /**
     * 获取新版计划
     *
     * @param planId
     * @return
     */
    InvestPlan getInvestPlan(int planId);

    /**
     * 投资新计划
     *
     * @throws Throwable
     */
    void doInvestPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable;

    /**
     * 投资新计划（存管）
     *
     * @throws Throwable
     */
    void doInvestPlanForCG(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable;

    /**
     * @param ownsStatus   持有状态 (1:持有中 2:申请退出 3:已退出)
     * @param interestTime 投资时间
     * @param exitTime     退出时间
     * @return
     */
    Integer getPlanMonthNum(int ownsStatus, long interestTime, long exitTime);

    /**
     * 用户申请退出计划
     * @return
     */
    Timestamp quitUserPlan(PlanRecordInfo planRecordInfo, BigDecimal expectedProfitPlan);

    /**
     * 用户的所有投资计划记录
     * @param userId
     * @param pageBounds
     * @return
     */
    List<PlanInvestInfo> getAllMyPlan(Integer userId, VersionTypeEnum vte, PageBounds pageBounds);

    /**
     * 计算新计划的当月年化
     * @param investInfo
     * @return
     */
    double getNewPlanRate(PlanInvestInfo investInfo);

    /**
     * 用户的退出中的计划
     * @param userId
     * @param pageBounds
     * @return
     */
    List<PlanInvestInfo> getMyQuitPlan(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds);

    /**
     * 成功退出的计划
     * @param userId
     * @param pageBounds
     * @return
     */
    List<PlanInvestInfo> getMySuccessPlans(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds);
    /**
     * 是否在退出中
     * @param userPlanId
     * @return
     */
    boolean checkExitingPlan(int userPlanId);
}

