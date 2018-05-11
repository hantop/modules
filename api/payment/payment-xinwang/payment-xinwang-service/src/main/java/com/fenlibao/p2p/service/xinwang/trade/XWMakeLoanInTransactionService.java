package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectRate;
import com.fenlibao.p2p.model.xinwang.entity.project.XWRepaymentPlan;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2018/2/1.
 */
public interface XWMakeLoanInTransactionService {

    /**
     * 添加成交服务费的流水
     * @param projectInfo
     * @param extraInfo
     */
    public void addXWCapitalFlowOfServiceFee(XWProjectInfo projectInfo, XWProjectExtraInfo extraInfo);

    /**
     * 添加成交服务费的还款计划
     * @param projectInfo
     * @param credit
     * @param endDate
     * @param projectRate
     * @param extraInfo
     * @return
     */
    ArrayList<XWRepaymentPlan> addXWRepaymentPlanWithServiceFee(XWProjectInfo projectInfo, SysCredit credit, Date endDate, XWProjectRate projectRate, XWProjectExtraInfo extraInfo) ;
}


