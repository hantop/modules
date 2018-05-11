package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectPrepaymentConfig;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.vo.RepayBudgetVO;
import com.fenlibao.p2p.service.xinwang.XWBatchNotifyService;

/**
 * 还款
 */
public interface XWRepayService extends XWBatchNotifyService {
    /**
     * 获取还款预算
     * @param projectId 标id
     * @param type 到期还款or提前还
     * @param compensatory 是否代偿
     * @return
     * @throws Exception
     */
    RepayBudgetVO getRepayBudget(Integer projectId, SysRepayOperationType type,Boolean compensatory) throws Exception;

    /**
     * 还款申请
     * @param projectId
     * @param type
     * @param compensatory
     * @throws Exception
     */
    void repayApply(Integer projectId, SysRepayOperationType type,Boolean compensatory,XWProjectPrepaymentConfig xwProjectPrepaymentConfig) throws Exception;

    /**
     * 处理还款异常
     * @param orderId
     * @throws Exception
     */
    public void handleError(Integer orderId)throws Exception;

    /**
     * 标是否还完
     * @param projectInfo
     * @throws Exception
     */
    public void checkIfRepayFinish(XWProjectInfo projectInfo)throws Exception;

    /**
     * 保存提前还款违约金配置信息
     * @param xwProjectPrepaymentConfig
     */
    void saveXWProjectPrepaymentConfig(XWProjectPrepaymentConfig xwProjectPrepaymentConfig);
}
