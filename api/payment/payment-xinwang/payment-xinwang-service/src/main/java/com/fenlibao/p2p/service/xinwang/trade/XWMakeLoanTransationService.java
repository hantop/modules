package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectRate;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/6.
 */
public interface XWMakeLoanTransationService {
    void confirmTenderAcceptFail(XWRequest request, Integer orderId, String resultJson)throws Exception;
    List<Map<String,Object>> buildBatchs(Integer projectId, List<XWTenderRecord> tenderRecordList, XWProjectRate projectRate, Integer orderId, Integer batchNum,XWProjectInfo projectInfo)throws Exception;
    void platformMakeLoan(String requestNo,Integer orderId)throws Exception;
    void generateRepaymentPlan(XWProjectInfo projectInfo, Date bearInterestDate, Date endDate, XWProjectExtraInfo extraInfo, XWProjectRate projectRate) throws Exception;
    void sendLetterAndMsg(XWProjectInfo projectInfo);

    /**
     * 放款成功后，给投资用户发送站内消息
     * @param projectInfo
     */
    void sendLetterAfterMakeLoan(XWProjectInfo projectInfo);


}
