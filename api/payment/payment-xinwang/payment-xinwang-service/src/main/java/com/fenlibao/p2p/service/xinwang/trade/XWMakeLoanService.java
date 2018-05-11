package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.service.xinwang.XWBatchNotifyService;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/23.
 */
public interface XWMakeLoanService extends XWBatchNotifyService{
    void makeLoanApply(Integer loanId) throws Exception;
    /** 没有维护了 **/
    @Deprecated
    void handleError(Integer orderId)throws Exception;
    void checkIfFinish(Integer bidId)throws Exception;
    Integer getOngoingConfirmTenderOrder(Integer projectId);
}
