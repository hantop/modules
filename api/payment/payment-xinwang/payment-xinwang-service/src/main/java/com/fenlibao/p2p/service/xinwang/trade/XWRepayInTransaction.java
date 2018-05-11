package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;

import java.util.Date;
import java.util.List;

/**
 * 在XWRepayTransactionServiceImpl调用的方法抽象出来，因为this.XX没有事务作用
 * Created by Administrator on 2018/1/29.
 */
public interface XWRepayInTransaction {

    /**
     * 提前还款本期需要更改的 利息，利息管理费，标加息，加息券加息；
     * 最后把大于本期的非本金和成交服务费改为已还，金额为0
     * @param projectId
     * @param originList
     * @param projectInfo
     * @param projectExtraInfo
     * @param projectRate
     * @param currentTerm
     * @param currentDate
     * @param modifyDatabase
     * @param xwProjectPrepaymentConfig
     * @param type
     * @return
     * @throws Exception
     */
    List<XWRepaymentPlan> modifyRepaymentPlan(Integer projectId, List<XWRepaymentPlan> originList, XWProjectInfo projectInfo, XWProjectExtraInfo projectExtraInfo, XWProjectRate projectRate, Integer currentTerm, Date currentDate, boolean modifyDatabase, XWProjectPrepaymentConfig xwProjectPrepaymentConfig, SysRepayOperationType type)throws Exception;



    List<SysCreditToRepay> transfromDataStructure(Integer orderId, Integer projectId, List<XWRepaymentPlan> repaymentPlanList, SysRepayOperationType type, XWProjectInfo projectInfo, XWProjectRate projectRate, Integer currentTerm)throws Exception;
}
