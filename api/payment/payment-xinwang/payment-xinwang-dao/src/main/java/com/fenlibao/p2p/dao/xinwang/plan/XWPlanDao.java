package com.fenlibao.p2p.dao.xinwang.plan;

import com.fenlibao.p2p.model.xinwang.entity.plan.SysPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlanCredit;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysPlanPayeeReceivePayment;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/6.
 */
public interface XWPlanDao {
    void fillCreditIdByTenderId(Map<String,Object> params);
    SysUserPlanCredit getUserPlanCreditByCreditId(Integer creditId);
    SysUserPlan getUserPlanById(Integer id);
    SysPlan getPlanById(Integer id);
    void updateUserPlanCreditReturnAmount(Map<String,Object> params);
    List<SysPlanPayeeReceivePayment> sumBidPlanData(Integer planId);
}
