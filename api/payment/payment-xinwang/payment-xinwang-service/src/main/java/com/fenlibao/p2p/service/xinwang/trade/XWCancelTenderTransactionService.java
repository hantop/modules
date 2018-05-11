package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */
public interface XWCancelTenderTransactionService {
    void finishCancelTender(Integer projectId,Integer orderId,Date currentDate)throws Exception;
    void platformCancelTender(Integer orderId, Integer projectId, XWProjectInfo projectInfo, List<XWTenderRecord> cancelSuccessTenderRecordList)throws Exception;
}
