package com.fenlibao.p2p.service.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.model.xinwang.enums.common.XWCapitalFlowLevel;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/6.
 */
public interface XWRepayTransactionService {
    List<Map<String,Object>> buildMarketingBatchs(XWProjectInfo projectInfo, List<SysCreditToRepay> actualRepayDetailList, Integer orderId)throws Exception;
    void batchRepayRequestAcceptSuccess(String batchNo, Map<String,Object> batch)throws Exception;
    void batchMarketingRequestAcceptSuccess(String batchNo, Map<String,Object> batch)throws Exception;
    void batchRequestAcceptFail(String batchNo, String resultJson,List<String> requestNos)throws Exception;
    void xinwangPretreatmentSuccess(String preTreatRequestNo,SysProjectRepayInfo projectRepayInfo)throws Exception;
    List<Map<String,Object>> buildRepayBatchs(XWProjectInfo projectInfo,List<SysCreditToRepay> actualRepayDetailList,String preTreatRequestNo,Integer orderId,Boolean compensatory)throws Exception;
    List<SysCreditToRepay> platformPretreatment(Integer projectId, XWProjectInfo projectInfo, SysRepayOperationType type, SysProjectRepayInfo projectRepayInfo, Integer orderId, Boolean compensatory, Date currentDate,XWProjectPrepaymentConfig xwProjectPrepaymentConfig)throws Exception;
    void xinwangRepayFinish(String requestNo,XWTradeType tradeType)throws Exception;
    void platfromRepay(Integer projectId,XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,List<String> repaySuccessRequestList)throws Exception;
    void projectRepayFinish(Integer projectId)throws Exception;
    void platfromMarketing(Integer projectId,XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,List<String> marketingSuccessRequestList)throws Exception;
    List<XWRepaymentPlan> generatePrepaymentPlan(Integer projectId, XWProjectInfo projectInfo, XWProjectExtraInfo projectExtraInfo, XWProjectRate projectRate, Integer currentTerm, Date currentDate, boolean modifyDatabase, XWProjectPrepaymentConfig xwProjectPrepaymentConfig, SysRepayOperationType type)throws Exception;
    /**
     * 发信息
     * @param projectId
     * @param projectInfo
     * @param projectRepayInfo
     * @throws Exception
     */
    void sendLetterAndMsg(Integer projectId, XWProjectInfo projectInfo, SysProjectRepayInfo projectRepayInfo) throws Exception;
    void ifCurrentTermRepayFinish(Integer projectId,Integer term,Integer orderId)throws Exception;

    /**
     * 锁t6230,并且修改状态
     * @param projectInfo
     */
    Integer saveApplyInfo(XWProjectInfo projectInfo);

    /**
     * 添加/启动异常还款订单
     * @param orderId
     * @param bidId
     */
    void addUnusualRepay(Integer orderId, Integer bidId);

    /**
     * 添加6102 交易流水
     * @param outAccount 支出账户
     * @param inAccount 收入账户
     * @param amount 交易金额
     * @param projectId 标id
     * @param orderId 订单id
     * @param t6102sToInsert
     * @param remark 备注信息
     * @param tradeType 交易类型
     * @param level 流水级别
     */
    void addXWCapitalFlow(XWFundAccount outAccount, XWFundAccount inAccount, BigDecimal amount, Integer projectId, Integer orderId, List<XWCapitalFlow> t6102sToInsert, String remark, int tradeType, XWCapitalFlowLevel level);
}
