package com.fenlibao.p2p.dao.user;

import com.fenlibao.p2p.model.entity.bid.UserBidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserNewCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserPlanInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 用户自动投设置
 */
public interface AutoMatchBidDao {

    /**
     * 从用户投资计划表获取已经投资了的记录
     */
    UserPlanInfo getSuitableUserPlanInfo(Timestamp dbCurrentTime, Timestamp lastCreateTime, VersionTypeEnum versionTypeEnum);

    /**
     * 从用户投资计划表获取投资记录
     */
    UserPlanInfo getSuitableUserPlanInfoById(int userPlanId);

    /**
     * 记录加锁
     */
    int lockUserPlan(int userPlanId);

    /**
     * 查询从计划中退出的债权列表
     */
    List<UserCreditInfoForPlan> getPlanCreditList(String type, int userId);

    /**
     * 获取债权转让后的信息
     */
    UserNewCreditInfoForPlan getNewCreditInfo(int applyforId);

    /**
     * 保存将购买记录
     */
    void insertUserPlanProduct(Map<String, Object> map);

    /**
     * 查询标列表
     */
    List<UserBidInfoForPlan> getPlanBidList(String type, int userId, int cgMode);

    /**
     * 查询标列表
     */
    List<UserBidInfoForPlan> getPlanBidList(String type, int userId, UserPlanInfo userPlanInfo);
}
