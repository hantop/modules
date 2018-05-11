package com.fenlibao.p2p.service.bid;

import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
public interface PlanBidService {

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
}
