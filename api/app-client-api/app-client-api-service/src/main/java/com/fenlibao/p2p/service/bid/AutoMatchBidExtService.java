package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.bid.UserCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserPlanInfo;
import com.fenlibao.p2p.model.vo.bid.PlanBidVO;

public interface AutoMatchBidExtService {
    @Deprecated
    int doBidForPlan(PlanBidVO planBidVO, UserPlanInfo userPlanInfo) throws Throwable;

    void doCreditForPlan(UserCreditInfoForPlan userCreditInfoForPlan, UserPlanInfo userPlanInfo) throws Throwable;

}

