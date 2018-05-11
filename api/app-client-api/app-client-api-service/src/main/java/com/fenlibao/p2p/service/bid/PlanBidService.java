package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
public interface PlanBidService {

    /**
     * 允许自动发布计划
     * @return
     */
    boolean enabledReleasePlan();

    /**
     * 发布计划
     */
    void preparedReleasePlan() throws Exception;

    /**
     * 购买计划同时配标
     * @param planId
     * @param amount
     * @param accountId
     * @param fxhbIds // 红包ids
     * @param jxqId  加息券Id
     * @return
     * @throws Exception
     */
    boolean doInvestPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId) throws Exception;

    /**
     * 自动封，低于100封，手动封，
     * @param planId
     * @param pmsUserId
     */
    void terminationPlan(Integer planId, int pmsUserId) throws Exception;
}
