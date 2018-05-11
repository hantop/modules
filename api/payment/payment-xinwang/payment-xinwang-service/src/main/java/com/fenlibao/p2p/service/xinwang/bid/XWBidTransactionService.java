package com.fenlibao.p2p.service.xinwang.bid;

import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.enums.bid.ProductTypeForCG;

public interface XWBidTransactionService {
    void doSuccess(int orderId, String requestNo, Integer jxqId, ProductTypeForCG productType, String... redpacketIdsArr) throws Exception;

    void doFail(int orderId,String method, String requestNo,String errorCode,String errorMsg);

    void doSubmit(int orderId, String requestNo);

    void doSuccessForPlan(Integer orderId, XWTenderBO tender, XWProjectInfo projectInfo);
}
