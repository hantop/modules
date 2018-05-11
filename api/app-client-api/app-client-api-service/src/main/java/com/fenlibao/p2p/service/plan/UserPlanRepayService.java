package com.fenlibao.p2p.service.plan;

public interface UserPlanRepayService {
   /**
    * 创建计划预期还款计划,计划改状态为还款中时调用
    * @param planId
    */
   void createExpectedRepayPlan(int planId) throws Exception; 
}
