package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacingInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface PlanService {

    /**
     * 查询计划
     *
     * @param lessThanSurplusAmount 可投金额小于
     * @param nowTime
     * @param status
     * @return
     */
    List<Integer> findPlanIds(int lessThanSurplusAmount, Date nowTime, int status);

    /**
     * 终止
     *
     * @param planId     计划ID
     * @param operatorId PMS用户ID
     */
    void termination(int planId, int operatorId) throws Exception;

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
     * 投资新版计划(存管)
     *
     * @throws Throwable
     */
    void doInvestPlanForCG(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable;

    /**
     * 获取计划
     *
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
     * 获取待封标(DFB)计划ID列表
     *
     * @return
     */
    List<AutoTenderVO> getPlanIdList_TBZ();

    /**
     * 根据计划ID获取待封标的标
     *
     * @param planId
     * @return
     */
    List<AutoTenderVO> getBidListByPlanId(int planId);

    /**
     * 将计划状态更改为待放款状态
     *
     * @param planId
     * @return
     */
    int updatePlanState(int planId);

    /**
     * 是否是计划的标
     *
     * @param bidId
     * @return
     */
    Integer getPlanIdByBidId(int bidId);

    /**
     * @param ownsStatus   持有状态 (1:持有中 2:申请退出 3:已退出)
     * @param interestTime 投资时间
     * @param exitTime     退出时间
     * @return
     */
    Integer getPlanMonthNum(int ownsStatus, long interestTime, long exitTime);

    /**
     * 投资新计划
     *
     * @throws Throwable
     */
    void doInvestPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable;

    /**
     * 用户申请退出计划
     *
     * @param planRecordInfo
     * @param expectedProfitPlan
     * @return
     */
    Timestamp quitUserPlan(PlanRecordInfo planRecordInfo, BigDecimal expectedProfitPlan);

    /**
     * 用户的新计划债权信息
     *
     * @param userId
     * @param isUp
     * @param timestamp
     * @param status
     * @return
     */
    List<PlanFinacingInfo> getPlanFinacing(int userId, int isUp, String timestamp, String status);
    List<PlanFinacingInfo> getPlanFinacing(int userId, int isUp, String timestamp, String status,int versionTypeEnum);

    /**
     * 计算当前月的计划利率
     *
     * @param investInfo
     * @return
     */
    BigDecimal getNewPlanRate(PlanFinacingInfo investInfo);

    /**
     * 是否在退出中
     *
     * @param userPlanId
     * @return
     */
    boolean checkExitingPlan(int userPlanId);

    /**
     * 获取计划的类型
     * @param planId
     * @return
     */
    VersionTypeEnum getPlanVersion(int planId);

    List<Integer> getPlanInfoNotFull(Date date ,int isCg) throws Exception;
}

