package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.plan.PlanBid;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
public interface PlanOrderService {

    /**
     * 插入异常日志
     * @param orderId
     * @param th
     */
    void addErrorLog(int orderId, Throwable th) throws Exception;

    /**
     * 创建买表订单
     * @param accountId
     * @param planBid
     * @param amount
     * @return
     */
    Map<String,String> createInvestBidOrder(int accountId, PlanBid planBid, BigDecimal amount);

    /**
     * 创建投资计划订单
     * @return
     */
    Integer createInvestPlanOrder(Integer accountId, Integer planId, BigDecimal investAmount);

    void submitKernel(int orderId);

    void confirmKernel(int orderId, Map<String, String> rtnMap);

    void updatePlanOrder(String sb, Integer orderId);

    /**
     * 回填用户投资计划id到订单
     * @param userPlanId
     * @param orderId
     */
    void returnBackUserPlanId(int userPlanId, Integer orderId);

    /**
     * 冻结用户资金
     * @param orderId
     * @param number
     * @param title
     * @param accountId
     * @param amount
     */
    void lockUserAmountForPlan(int orderId, String number, String title, int accountId, BigDecimal amount) throws Exception;
}
