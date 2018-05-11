package com.fenlibao.service.pms.da.biz.investplan;

import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
public interface PlanBidService {

    /**
     * 自动封，低于100封，手动封，
     * @param planId
     * @param pmsUserId
     */
    void terminationPlan(Integer planId, int pmsUserId) throws Exception;
}
