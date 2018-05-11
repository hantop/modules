package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.bid.DirectionalPlan;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;

import java.util.List;

/**
 * 计划信息service
 * @author Mingway.Xu
 * @date 2017/3/22 11:48
 */
public interface PlanInfoService {
    /**
     * 检验用户是否可以投计划
     * @param productId
     * @param userId
     * @return
     */
    void checkCanInvest(int productId, int userId, VersionTypeEnum versionTypeEnum);

    /**
     * 获取产品的计划条件
     * @param productId
     * @return
     */
    DirectionalPlan getDirectionalPlan(int productId);

    /**
     * 获取用户计划信息
     * @param planRecordId
     * @param integer
     * @return
     */
    PlanRecordInfo getPlanRecordInfo(int planRecordId, Integer integer);

    /**
     * 获取产品库中的产品信息
     * @param userPlanId
     * @return
     */
    List<UserPlanProduct> getUserPlanProducts(int userPlanId);

    /**
     * 锁用户投资计划记录
     * @param userPlanId
     */
    void lockUserPlan(int userPlanId);
}
