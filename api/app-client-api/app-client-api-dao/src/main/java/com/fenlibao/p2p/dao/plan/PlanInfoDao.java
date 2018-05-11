package com.fenlibao.p2p.dao.plan;

import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanTotalRepay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface PlanInfoDao {
    InvestPlanInfo getPlanInfo(int id);
    List<UserPlan> getUserPlansByPlanId(int planId);
    UserCouponInfo getUserCoupon(int userPlanId);
    void batchInsertUserPlanRepayPlan(List<UserPlanTotalRepay> list);

    /**
     * 终止计划
     * @param planId
     * @param expireTime
     * @param bearrateDate
     * @param status
     * @param fullTime
     * @param updateTime
     */
    void updatePlanTermination(int planId, Date expireTime, Date bearrateDate, int status, Date fullTime, Date updateTime);

    /**
     * 新增计划_终止记录
     *
     * @param planId
     * @param operatorId
     */
    void insertPlanTermination(int planId, int operatorId);

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
     * 获取计划未满标
     */
    List<Integer>  getPlanInfoNotFull(HashMap map);
}
