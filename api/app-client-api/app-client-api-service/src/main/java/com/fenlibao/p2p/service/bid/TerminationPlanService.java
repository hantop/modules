package com.fenlibao.p2p.service.bid;

/**
 * 终止计划
 * <p>
 * Created by chenzhixuan on 2017/4/11.
 */
public interface TerminationPlanService {
    /**
     * 终止可投金额小于lessThanSurplusAmount的计划
     *
     */
    void terminationByLessThanSurplusAmount() throws Exception;

    /**
     * 终止人
     * @return
     */
    int getPmsUserId();
}
