package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.vo.plan.BidForPlanVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zeronx on 2017/11/21 18:16.
 * @version 1.0
 * 计划购买标辅助类
 */
public interface PlanBidMatchBidService {

    /**
     *
     * 计划配标
     * @param planId 计划id
     * @param userPlanId 用户投资计划记录id
     * @param accountId 用户id
     * @param amount 投资计划金额
     * @param bidForPlanVOs 准备购买的标
     * @return
     */
    BigDecimal investBidForPlan(int planId, int userPlanId, int accountId, BigDecimal amount, List<BidForPlanVO> bidForPlanVOs);
}
