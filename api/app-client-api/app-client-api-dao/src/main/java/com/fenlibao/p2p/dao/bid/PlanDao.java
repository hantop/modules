package com.fenlibao.p2p.dao.bid;

import com.fenlibao.p2p.model.entity.bid.DirectionalPlan;
import com.fenlibao.p2p.model.entity.finacing.PlanFinacingInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.entity.plan.UserPlanProduct;
import com.fenlibao.p2p.model.vo.bidinfo.AutoTenderVO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by zcai on 2017/2/16.
 */
public interface PlanDao {

    /**
     * 获取待封标(DFB)计划ID列表
     * @return
     */
    List<AutoTenderVO> getPlanIdList_TBZ();

    /**
     * 根据计划ID获取待封标的标
     * @param planId
     * @return
     */
    List<AutoTenderVO> getBidListByPlanId(int planId);

    /**
     * 将计划状态更改为待放款状态
     * @param planId
     * @return
     */
    int updatePlanState(int planId);
    /**
     * 是否是计划的标
     * @param bidId
     * @return
     */
    Integer getPlanIdByBidId(int bidId);

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
    PlanRecordInfo getPlanRecordInfo(int planRecordId, Integer userId);

    /**
     * 获取产品库中的产品信息
     * @param userPlanId
     * @return
     */
    List<UserPlanProduct> getUserPlanProducts(int userPlanId);

    /**
     * 扣除计划可投金额
     * @return
     */
    int updateSurplusAmountForInvestPlan(int planId, BigDecimal amount);

    /**
     * 增加投资记录
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
     * 持有
     * @param userId
     * @param isUp
     * @param time
     * @return
     */
    List<PlanFinacingInfo> getHoldPlan(int userId, int isUp, Date time);
    List<PlanFinacingInfo> getHoldPlan(int userId, int isUp, Date time,int versionTypeEnum);

    /**
     * 退出中
     * @param userId
     * @param isUp
     * @param time
     * @return
     */
    List<PlanFinacingInfo> getQuitPlan(int userId, int isUp, Date time);
    List<PlanFinacingInfo> getQuitPlan(int userId, int isUp, Date time,int versionTypeEnum);

    /**
     * 已回款
     * @param userId
     * @param isUp
     * @param time
     * @return
     */
    List<PlanFinacingInfo> getProfitPlan(int userId, int isUp, Date time);
    List<PlanFinacingInfo> getProfitPlan(int userId, int isUp, Date time,int versionTypeEnum);

    /**
     * 获取用户退出中的计划
     * @param userPlanId
     * @return
     */
    UserPlan getExitPlan(int userPlanId);

    /**
     * 获取计划的存管类型
     * @param planId
     * @return
     */
    Integer getVersionType(int planId);
}
