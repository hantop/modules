package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.bid.UserNewCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserPlanInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;

import java.sql.Timestamp;

/**
 *
 */
public interface AutoMatchBidService {


    UserPlanInfo getUserPlanInfo(Timestamp dbCurrentTime, Timestamp lastCreateTime, VersionTypeEnum versionTypeEnum);

    /**
     * 普通版计划配标
     */
    UserPlanInfo autoMatchBidForNormalPlan(UserPlanInfo userPlanInfo);

    /**
     * 存管版计划配标
     */
    @Deprecated
    UserPlanInfo autoMatchBidForCGPlan(UserPlanInfo userPlanInfo);

    /**
     * 获取数据库时间
     *
     * @return
     */
    Timestamp getDBCurrentTime();

    /**
     * 插入新的投资记录
     * @param userNewCreditInfoForPla
     * @param userPlanInfo
     */
    void insertNewCredit(UserNewCreditInfoForPlan userNewCreditInfoForPla, UserPlanInfo userPlanInfo);

    /**
     * 从用户投资计划表获取投资记录
     */
    UserPlanInfo getSuitableUserPlanInfoById(int userPlanId);

    /**
     * 获取购买后新的债权信息
     * @param applyforId
     * @return
     */
    UserNewCreditInfoForPlan getNewCreditInfo(int applyforId);
}

