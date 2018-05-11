package com.fenlibao.dao.pms.da.biz.investplan;

import com.fenlibao.model.pms.da.biz.plan.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/11/16 16:04.
 * @version 1.0
 */
public interface PlanBidMapper {

    /**
     *
     * @param planId
     * @param isGetAllPlanBindBids true:获取计划绑定的所有处于投资投资中的标的、false:获取计划绑定且已被投资但未满的标的
     * @return
     */
    List<BidForPlanVO> getPlanBindBids(@Param("planId") int planId, @Param("isGetAllPlanBindBids") boolean isGetAllPlanBindBids);

    /**
     * 锁标记录
     * @param bidId
     * @return
     */
    PlanBid lockBidById(@Param("bidId") Integer bidId);

    /**
     * 插入系统订单
     * @return
     */
    void addSysOrder(SysOrderVO sysOrderVO);

    /**
     * 插入投机划订单
     * @param ordId
     * @param accountId
     * @param planId
     * @param investAmount
     */
    void addPlanOrder(@Param("orderId") int ordId, @Param("accountId") int accountId, @Param("planId") Integer planId, @Param("investAmount") BigDecimal investAmount);

    /**
     * 插入投标记录
     * @param investBidRecordMap
     * @return
     */
    Integer addInvestBidRecord(Map<String, Object> investBidRecordMap);

    /**
     * 更新标状态和可投金额
     * @param updateBids
     */
    void updateBids(List<PlanBid> updateBids);

    /**
     * 设置标的满标时间为当前执行时间
     * @param updateBidFullTimes
     */
    void updateBidFullTimes(List<Integer> updateBidFullTimes);

    /**
     * 添加新网投标记录表记录
     * @param addXwTenders
     * @return
     */
    void addXwTenders(List<XwTenderVO> addXwTenders);

    /**
     * 回填计划配标记录
     * @param addPlanProducts
     */
    void addPlanProducts(List<PlanProductVO> addPlanProducts);

    /**
     * 添加购买计划异常订单日志
     * @param params
     */
    void addErrorLog(Map<String, String> params);

    void updatePlanOrder(Map<String, Object> params);

    /**
     * 根据订单Id锁订单
     * @param orderId
     * @return
     */
    SysOrderVO lockOrderById(@Param("orderId") int orderId);

    /**
     * 回填用户购买计划记录id到计划订单
     * @param params
     */
    void returnBackUserPlanId(Map<String, Object> params);

    /**
     * 根据code 获取标类型Id
     * @param code
     * @return
     */
    int getBidTypeByCode(@Param("code") String code);

    /**
     * 根据用户资金Id锁用户资金账号记录（单条）
     * @param accountId
     * @return
     */
    UserAccount lockUserAccountById(@Param("accountId") int accountId);

    void updateUserAmount(@Param("balance") BigDecimal balance, @Param("accountId") int accountId);

    /**
     * 添加资金交易流水
     * @param tradeRecord
     */
    void addTradeRecord(TradeRecord tradeRecord);

    /**
     * 解绑计划绑定的标
     * @param planId
     */
    void unBindPlanBids(@Param("planId") Integer planId);

    InvestPlanVO lockInvestPlanById(@Param("planId") Integer planId);

    UserAccount getUserAccount(@Param("userId") Integer userId, @Param("type") String type);

    int updateSurplusAmountForInvestPlan(@Param("planId") int planId, @Param("amount") BigDecimal amount);

    void insertRecordForInvestPlan(InvestUserPlan investUserPlan);

    /**
     * 获取特殊账户ids
     * @param uType
     * @return
     */
    List<String> getUserIds(@Param("uType") Integer uType);

    String getBidBorrowerUserNoBy(@Param("bidId") Integer bidId);

}
