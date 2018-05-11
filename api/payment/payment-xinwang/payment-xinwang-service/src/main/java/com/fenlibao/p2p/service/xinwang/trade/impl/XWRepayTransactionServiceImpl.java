package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.coupon.SysCouponManageDao;
import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.plan.XWPlanDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.consts.SysVariableConsts;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCreditTransferApply;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.order.UnusualRepay;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlan;
import com.fenlibao.p2p.model.xinwang.entity.plan.SysUserPlanCredit;
import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysPlanPayeeReceivePayment;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.SysMsgSendType;
import com.fenlibao.p2p.model.xinwang.enums.common.XWCapitalFlowLevel;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.credit.CreditTransferApply_Status;
import com.fenlibao.p2p.model.xinwang.enums.credit.SysCredit_Transfering;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.order.XWTradeOrderType;
import com.fenlibao.p2p.model.xinwang.enums.plan.SysPlan_Type;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.ProjectExtraInfo_Overdue;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentWay;
import com.fenlibao.p2p.model.xinwang.enums.trade.*;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.common.RedisUtilService;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayInTransaction;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Administrator on 2017/7/6.
 */
@Service
public class XWRepayTransactionServiceImpl implements XWRepayTransactionService{

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    SysRepayDao repayDao;

    @Resource
    PTCommonDao commonDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    SysCreditDao creditDao;

    @Resource
    SysCouponManageDao couponDao;

    @Resource
    XWPlanDao planDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysOrderService orderService;

    @Resource
    PTCommonDao ptCommonDao;

    @Resource
    PTCommonService ptCommonService;

    @Resource
    XWCreditService xwCreditService;

    @Resource
    SysOrderManageDao orderManageDao;

    @Resource
    XWRepayInTransaction xWRepayInTransaction;

    @Resource
    RedisUtilService redisUtilService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Map<String,Object>> buildMarketingBatchs(XWProjectInfo projectInfo, List<SysCreditToRepay> actualRepayDetailList, Integer orderId)throws Exception{
        Date requestTime=new Date();
        SysProjectRepayInfo projectRepayInfo=repayDao.getProjectRepayInfoByOrderId(orderId);
        //挑出需要发奖励的债权
        List<SysCreditToRepay> creditMarketingList=new ArrayList<>();
        for(SysCreditToRepay actualRepayDetail:actualRepayDetailList){
            //reward是所有平台给投资人的奖励的和
            BigDecimal reward=actualRepayDetail.getTenderIncreaseInterest().add(actualRepayDetail.getProjectIncreaseInterest());
            if(reward.compareTo(BigDecimal.ZERO)>0){
                creditMarketingList.add(actualRepayDetail);
            }
            //把为0的加息还款计划置为已还
            if(actualRepayDetail.getTenderIncreaseInterest().compareTo(BigDecimal.ZERO)<=0){
                //将债权对应的还款计划改为已还
                Map<String,Object> updateRepaymentPlanParams=new HashMap<>();
                updateRepaymentPlanParams.put("creditId",actualRepayDetail.getCreditId());
                List<Integer> feeTypeList=new ArrayList<>();
                feeTypeList.add(SysTradeFeeCode.TZ_JX);
                updateRepaymentPlanParams.put("feeTypeList",feeTypeList);
                if(projectRepayInfo.getRepayType()==SysRepayOperationType.REPAY){
                    updateRepaymentPlanParams.put("term",projectRepayInfo.getTerm());
                }
                repayDao.finishRepaymentPlanOfCredit(updateRepaymentPlanParams);
            }
            if(actualRepayDetail.getProjectIncreaseInterest().compareTo(BigDecimal.ZERO)<=0){
                //将债权对应的还款计划改为已还
                Map<String,Object> updateRepaymentPlanParams=new HashMap<>();
                updateRepaymentPlanParams.put("creditId",actualRepayDetail.getCreditId());
                List<Integer> feeTypeList=new ArrayList<>();
                feeTypeList.add(SysTradeFeeCode.BID_JX);
                updateRepaymentPlanParams.put("feeTypeList",feeTypeList);
                if(projectRepayInfo.getRepayType()==SysRepayOperationType.REPAY){
                    updateRepaymentPlanParams.put("term",projectRepayInfo.getTerm());
                }
                repayDao.finishRepaymentPlanOfCredit(updateRepaymentPlanParams);
            }
        }
        //如果大于batch最大请求数，分到多个batch
        BigDecimal creditMarketingListSize=new BigDecimal(creditMarketingList.size());
        BigDecimal batchSize=new BigDecimal(XinwangConsts.MAX_REQUESTS_PER_BATCH);
        int batchNum = creditMarketingListSize.divide(batchSize, 0, RoundingMode.UP).intValue();
        List<Map<String,Object>> batchList=new ArrayList<>();
        for(int i=0;i<batchNum;i++){
            int startIndex=i*XinwangConsts.MAX_REQUESTS_PER_BATCH;
            int endIndex=i*XinwangConsts.MAX_REQUESTS_PER_BATCH+XinwangConsts.MAX_REQUESTS_PER_BATCH;
            endIndex=endIndex>creditMarketingList.size()?creditMarketingList.size():endIndex;
            List<SysCreditToRepay> creditMarketingInBatch=creditMarketingList.subList(startIndex,endIndex);
            String batchNo= XinWangUtil.createRequestNo();
            List<Map<String,Object>> bizDetails=new ArrayList<>();
            for(SysCreditToRepay creditMarketing:creditMarketingInBatch){
                List<Map<String,Object>> details=new ArrayList<>();
                //detail1是营销奖励金额
                BigDecimal reward=creditMarketing.getTenderIncreaseInterest().add(creditMarketing.getProjectIncreaseInterest());//reward是所有平台给投资人的奖励的和
                Map<String,Object> detail1=new HashMap<>();
                detail1.put("bizType", XWBizType.MARKETING.getCode());
                detail1.put("sourcePlatformUserNo", XinWangUtil.CONFIG.marketingAccount());
                detail1.put("targetPlatformUserNo", creditMarketing.getInvestorPlatformUserNo());
                detail1.put("amount",reward);
                details.add(detail1);
                Map<String,Object> request=new HashMap<>();
                String requestNo=XinWangUtil.createRequestNo();
                request.put("requestNo", requestNo);
                request.put("tradeType", XWTradeType.MARKETING.getCode());
                request.put("projectNo", projectInfo.getProjectNo());
                request.put("details", details);
                bizDetails.add(request);
                //创建新网订单
                XWRequest req=new XWRequest();
                req.setInterfaceName(XinwangInterfaceName.ASYNC_TRANSACTION.getCode());
                req.setBatchNo(batchNo);
                req.setRequestNo(requestNo);
                req.setRequestTime(requestTime);
                req.setState(XWRequestState.DTJ);
                req.setPlatformUserNo(XinWangUtil.CONFIG.marketingAccount());
                req.setOrderId(orderId);
                requestDao.createRequest(req);
                //保存请求参数
                XWResponseMessage requestParams=new XWResponseMessage();
                requestParams.setRequestNo(requestNo);
                requestParams.setBatchNo(batchNo);
                requestParams.setRequestParams(JSON.toJSONString(request));
                requestDao.saveRequestMessage(requestParams);
                //保存营销请求号
                Map<String,Object> creditRepayDetailParams=new HashMap<>();
                creditRepayDetailParams.put("id",creditMarketing.getId());
                creditRepayDetailParams.put("marketingRequestNo",requestNo);
                repayDao.updateCreditRepayDetail(creditRepayDetailParams);
            }
            Map<String,Object> batch=new HashMap<>();
            batch.put("batchNo",batchNo);
            batch.put("bizDetails",bizDetails);
            batch.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
            batchList.add(batch);
        }
        return batchList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRepayRequestAcceptSuccess(String batchNo, Map<String,Object> batch)throws Exception{
        //请求状态改成待确认
        XWRequest updatequestParam=new XWRequest();
        List<String> requests =(List<String>) batch.get("requestNos");
        updatequestParam.setRequestNos(requests);
        updatequestParam.setBatchNo(batchNo);
        updatequestParam.setState(XWRequestState.DQR);
        requestDao.updateRequest(updatequestParam);
        //更新债权还款状态
        List<Map<String,Object>> bizDetails=(List<Map<String,Object>>)batch.get("bizDetails");
        for(Map<String,Object> request:bizDetails){
            Map<String,Object> updateupdateCreditRepayDetailParams=new HashMap<>();
            updateupdateCreditRepayDetailParams.put("repayState", CreditToRepay_RepayState.ACCEPTED);
            updateupdateCreditRepayDetailParams.put("byRepayRequestNo",request.get("requestNo"));
            repayDao.updateCreditRepayDetail(updateupdateCreditRepayDetailParams);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchMarketingRequestAcceptSuccess(String batchNo, Map<String,Object> batch)throws Exception{
        //请求状态改成待确认
        XWRequest updatequestParam=new XWRequest();
        updatequestParam.setBatchNo(batchNo);
        updatequestParam.setState(XWRequestState.DQR);
        requestDao.updateRequest(updatequestParam);
        //更新债权还款状态
        List<Map<String,Object>> bizDetails=(List<Map<String,Object>>)batch.get("bizDetails");
        for(Map<String,Object> request:bizDetails){
            Map<String,Object> updateupdateCreditRepayDetailParams=new HashMap<>();
            updateupdateCreditRepayDetailParams.put("marketingState", CreditToRepay_RepayState.ACCEPTED);
            updateupdateCreditRepayDetailParams.put("byMarketingRequestNo",request.get("requestNo"));
            repayDao.updateCreditRepayDetail(updateupdateCreditRepayDetailParams);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRequestAcceptFail(String batchNo, String resultJson,List<String> requestNos)throws Exception{
        //保存返回报文
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNos(requestNos);
        responseParams.setBatchNo(batchNo);
        responseParams.setResponseMsg(resultJson);
        requestDao.saveResponseMessage(responseParams);
        //请求失败或处理失败
        XWRequest updatequestParam=new XWRequest();
        updatequestParam.setRequestNos(requestNos);
        updatequestParam.setBatchNo(batchNo);
        updatequestParam.setState(XWRequestState.SB);
        requestDao.updateRequest(updatequestParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void xinwangPretreatmentSuccess(String preTreatRequestNo,SysProjectRepayInfo projectRepayInfo)throws Exception{
        //更新还款进度
        projectRepayInfo.setRepayProgress(XWRepayProgress.XINWANG_FUND_FROZEN);
        Map<String,Object> params=new HashMap<>();
        params.put("id",projectRepayInfo.getId());
        params.put("repayProgress",XWRepayProgress.XINWANG_FUND_FROZEN);
        params.put("preTreatRequestNo",preTreatRequestNo);
        repayDao.updateProjectRepayInfo(params);
        //结束订单
        XWRequest param=new XWRequest();
        param.setRequestNo(preTreatRequestNo);
        param.setState(XWRequestState.CG);
        requestDao.updateRequest(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Map<String,Object>> buildRepayBatchs(XWProjectInfo projectInfo,List<SysCreditToRepay> actualRepayDetailList,String preTreatRequestNo,Integer orderId,Boolean compensatory)throws Exception{
        Date requestTime=new Date();
        //如果待还款债权数大于batch最大请求数，分到多个batch
        BigDecimal actualRepayDetailListSize=new BigDecimal(actualRepayDetailList.size());
        BigDecimal batchSize=new BigDecimal(XinwangConsts.MAX_REQUESTS_PER_BATCH);
        int batchNum = actualRepayDetailListSize.divide(batchSize, 0, RoundingMode.UP).intValue();
        List<Map<String,Object>> batchList=new ArrayList<>();
        for(int i=0;i<batchNum;i++){
            int startIndex=i*XinwangConsts.MAX_REQUESTS_PER_BATCH;
            int endIndex=i*XinwangConsts.MAX_REQUESTS_PER_BATCH+XinwangConsts.MAX_REQUESTS_PER_BATCH;
            endIndex=endIndex>actualRepayDetailList.size()?actualRepayDetailList.size():endIndex;
            List<SysCreditToRepay> actualRepayDetailInBatch=actualRepayDetailList.subList(startIndex,endIndex);
            String batchNo= XinWangUtil.createRequestNo();
            List<String> requestNos = new ArrayList<>();//该批次里面涉及的多个请求requestNo

            List<Map<String,Object>> bizDetails=new ArrayList<>();
            for(SysCreditToRepay actualRepayDetail:actualRepayDetailInBatch){
                List<Map<String,Object>> details=new ArrayList<>();
                //detail1是还款金额
                BigDecimal income=actualRepayDetail.getInterest().add(actualRepayDetail.getOverduePenalty()).add(actualRepayDetail.getPrepayPenalty());//income是所有借款人给投资人的收益的和
                BigDecimal repayAmount=actualRepayDetail.getPrincipal().add(income);
                Map<String,Object> detail1=new HashMap<>();
                if(compensatory){
                    detail1.put("bizType", XWBizType.COMPENSATORY.getCode());
                    detail1.put("sourcePlatformUserNo", projectInfo.getGuaranteePlatformUserNo());
                }
                else {
                    detail1.put("bizType", XWBizType.REPAYMENT.getCode());
                    detail1.put("sourcePlatformUserNo", projectInfo.getBorrowerPlatformUserNo());
                }
                detail1.put("freezeRequestNo",preTreatRequestNo);
                detail1.put("targetPlatformUserNo", actualRepayDetail.getInvestorPlatformUserNo());
                detail1.put("amount",repayAmount);
                detail1.put("income",income);
                details.add(detail1);
                //detail2佣金
                BigDecimal commission=actualRepayDetail.getOverdueCommission().add(actualRepayDetail.getServiceCharge());//commission是所有借款人给平台的费用的和
                commission = commission.add(actualRepayDetail.getDealFee());//commission加成交服务费
                commission=commission.add(actualRepayDetail.getInterestServiceFee());//commission加上利息管理费
                commission=commission.add(actualRepayDetail.getPenaltyDivide());//commission加上违约金分成


                if(commission.compareTo(BigDecimal.ZERO)>0){
                    Map<String,Object> detail2=new HashMap<>();
                    detail2.put("bizType", XWBizType.COMMISSION.getCode());
                    detail2.put("freezeRequestNo",preTreatRequestNo);
                    if(compensatory){
                        detail2.put("sourcePlatformUserNo",projectInfo.getGuaranteePlatformUserNo());
                    }
                    else{
                        detail2.put("sourcePlatformUserNo",projectInfo.getBorrowerPlatformUserNo());
                    }
                    detail2.put("amount",commission);
                    details.add(detail2);
                }
                Map<String,Object> request=new HashMap<>();
                String requestNo=XinWangUtil.createRequestNo();

                request.put("requestNo", requestNo);
                requestNos.add(requestNo);
                if(compensatory){
                    request.put("tradeType", XWTradeType.COMPENSATORY.getCode());
                }
                else{
                    request.put("tradeType", XWTradeType.REPAYMENT.getCode());
                }
                request.put("projectNo", projectInfo.getProjectNo());
                request.put("details", details);
                bizDetails.add(request);
                //创建新网订单
                XWRequest req=new XWRequest();
                req.setInterfaceName(XinwangInterfaceName.ASYNC_TRANSACTION.getCode());
                req.setBatchNo(batchNo);
                req.setRequestNo(requestNo);
                req.setRequestTime(requestTime);
                req.setState(XWRequestState.DTJ);
                if(compensatory){
                    req.setPlatformUserNo(projectInfo.getGuaranteePlatformUserNo());
                }
                else{
                    req.setPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
                }
                req.setOrderId(orderId);
                requestDao.createRequest(req);

                //保存请求参数
                XWResponseMessage requestParams=new XWResponseMessage();
                requestParams.setRequestNo(requestNo);
                requestParams.setBatchNo(batchNo);
                requestParams.setRequestParams(JSON.toJSONString(request));
                requestDao.saveRequestMessage(requestParams);
                //保存还款请求号
                Map<String,Object> creditRepayDetailParams=new HashMap<>();
                creditRepayDetailParams.put("id",actualRepayDetail.getId());
                creditRepayDetailParams.put("repayRequestNo",requestNo);
                repayDao.updateCreditRepayDetail(creditRepayDetailParams);
            }
            Map<String,Object> batch=new HashMap<>();
            batch.put("batchNo",batchNo);
            batch.put("requestNos",requestNos);
            batch.put("bizDetails",bizDetails);
            batch.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
            batchList.add(batch);
        }
        return batchList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysCreditToRepay> platformPretreatment(Integer projectId, XWProjectInfo projectInfo, SysRepayOperationType type, SysProjectRepayInfo projectRepayInfo, Integer orderId, Boolean compensatory, Date currentDate,XWProjectPrepaymentConfig xwProjectPrepaymentConfig)throws Exception{
        //标扩展信息
        XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(projectId);
        //标费率
        XWProjectRate projectRate=projectDao.getProjectRateById(projectId);
        //还款期号
        Integer currentTerm=repayDao.getCurrentTerm(projectId);
        //还款计划
        List<XWRepaymentPlan> repaymentPlanList=null;
        //到期还款
        if(type==SysRepayOperationType.REPAY){
            Map<String,Object> repaymentPlanParams=new HashMap<>();
            repaymentPlanParams.put("projectId",projectId);
            repaymentPlanParams.put("term",currentTerm);
            repaymentPlanParams.put("repayState", RepaymentPlan_RepayState.WH);
            repaymentPlanList= repayDao.getRepaymentPlan(repaymentPlanParams);
//            if(repaymentPlanList.get(0).getDueDate().compareTo(currentDate)>0){
//                throw new XWTradeException(XWResponseCode.TRADE_REPAY_NOT_TIME_YET);
//            }
        }
        //提前还款
        else{
            //借款超过一个月才能提前还 TODO: 放开
            if(projectInfo.getRepaymentWay()== RepaymentWay.YCFQ){
                long canPrepayDate=DateUtil.rollNaturalMonth(projectExtraInfo.getBearInterestDate().getTime(), 1);
                long nowDate=currentDate.getTime();
                if(nowDate<canPrepayDate){
                    throw new XWTradeException(XWResponseCode.TRADE_PREPAY_NOT_ENOUGH_A_MONTH);
                }
            }
            else {
                if(currentTerm<=1){
                    throw new XWTradeException(XWResponseCode.TRADE_PREPAY_NOT_ENOUGH_A_MONTH);
                }
            }

            List<XWRepaymentPlan> list=new ArrayList<>();
            //本期到期应还
            Map<String,Object> prepaymentPlanParams=new HashMap<>();
            prepaymentPlanParams.put("projectId",projectId);
            prepaymentPlanParams.put("term",currentTerm);
            prepaymentPlanParams.put("repayState",RepaymentPlan_RepayState.WH);
            List<XWRepaymentPlan> originList= repayDao.getRepaymentPlan(prepaymentPlanParams);
            //修改还款计划
            List<XWRepaymentPlan> currentTermPrepaymentPlans=xWRepayInTransaction.modifyRepaymentPlan(projectId,originList,projectInfo,projectExtraInfo,projectRate,currentTerm,currentDate,true,xwProjectPrepaymentConfig,type);
            list.addAll(currentTermPrepaymentPlans);

            //剩余本金以及成交服务费还款计划
            Map<String,Object> otherTermPrincipalParams=new HashMap<>();
            otherTermPrincipalParams.put("projectId",projectId);
            otherTermPrincipalParams.put("feeTypeList",new int[]{SysTradeFeeCode.TZ_BJ,SysTradeFeeCode.CJFWF});
            otherTermPrincipalParams.put("repayState",RepaymentPlan_RepayState.WH);
            otherTermPrincipalParams.put("notEqualTerm",currentTerm);
            List<XWRepaymentPlan> otherTermPrincipals=repayDao.getRepaymentPlan(otherTermPrincipalParams);
            list.addAll(otherTermPrincipals);
            repaymentPlanList=list;
        }

        //借款人应还总额
        BigDecimal borrowerTotalRepay = BigDecimal.ZERO;
        for (XWRepaymentPlan repaymentPlan:repaymentPlanList) {
            //加息不是由借款人来还
            if(repaymentPlan.getFeeType()!= SysTradeFeeCode.TZ_JX&&repaymentPlan.getFeeType()!=SysTradeFeeCode.BID_JX){
                borrowerTotalRepay = borrowerTotalRepay.add(repaymentPlan.getAmount());
            }
        }

        String payerUserNo=projectInfo.getBorrowerPlatformUserNo();
        Integer payerUserId=projectInfo.getBorrowerUserId();
        //如果代偿，用担保人账号
        if(compensatory){
            payerUserNo=projectInfo.getGuaranteePlatformUserNo();
            payerUserId=projectExtraInfo.getGuaranteeUserId();
        }
        XinwangAccount xinwangAccount=accountDao.getXinwangAccount(payerUserNo);
        //借款人往来账户
        String payerWLZHType="XW_"+xinwangAccount.getUserRole().getCode()+"_WLZH";
        XWFundAccount borrowerWLZH = accountDao.getFundAccount(payerUserId, SysFundAccountType.parse(payerWLZHType));
        if (borrowerWLZH == null) {
            throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);
        }
        //校验余额是否充足
        if(borrowerWLZH.getAmount().compareTo(borrowerTotalRepay)<0){
            throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK);
        }
        //借款人锁定账户
        String payerSDZHType="XW_"+xinwangAccount.getUserRole().getCode()+"_SDZH";
        XWFundAccount borrowerSDZH = accountDao.getFundAccount(payerUserId, SysFundAccountType.parse(payerSDZHType));
        if (borrowerSDZH == null) {
            throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);
        }
        //将借款人的还款总额冻结
        borrowerWLZH.setAmount(borrowerWLZH.getAmount().subtract(borrowerTotalRepay));
        XWCapitalFlow wlzhFlow=new XWCapitalFlow();
        wlzhFlow.setFundAccountId(borrowerWLZH.getId());
        wlzhFlow.setTadeType(SysTradeFeeCode.REPAY_LOCK);
        wlzhFlow.setOtherFundAccountId(borrowerSDZH.getId());
        wlzhFlow.setExpenditure(borrowerTotalRepay);
        wlzhFlow.setBalance(borrowerWLZH.getAmount());
        wlzhFlow.setRemark("借款人还款资金冻结");
        wlzhFlow.setProjectId(projectId);
        wlzhFlow.setOrderId(orderId);
        commonDao.insertT6102(wlzhFlow);
        Map<String,Object> wlzhParams=new HashMap<>();
        wlzhParams.put("id",borrowerWLZH.getId());
        wlzhParams.put("amount",borrowerWLZH.getAmount());
        accountDao.updateFundAccount(wlzhParams);

        borrowerSDZH.setAmount(borrowerSDZH.getAmount().add(borrowerTotalRepay));
        XWCapitalFlow sdzhFlow=new XWCapitalFlow();
        sdzhFlow.setFundAccountId(borrowerSDZH.getId());
        sdzhFlow.setTadeType(SysTradeFeeCode.REPAY_LOCK);
        sdzhFlow.setOtherFundAccountId(borrowerWLZH.getId());
        sdzhFlow.setIncome(borrowerTotalRepay);
        sdzhFlow.setBalance(borrowerSDZH.getAmount());
        sdzhFlow.setRemark("借款人还款资金冻结");
        sdzhFlow.setProjectId(projectId);
        sdzhFlow.setOrderId(orderId);
        commonDao.insertT6102(sdzhFlow);
        Map<String,Object> sdzhParams=new HashMap<>();
        sdzhParams.put("id",borrowerSDZH.getId());
        sdzhParams.put("amount",borrowerSDZH.getAmount());
        accountDao.updateFundAccount(sdzhParams);
        //调整数据结构
        List<SysCreditToRepay> actualRepayDetailList=xWRepayInTransaction.transfromDataStructure(orderId,projectId,repaymentPlanList,type,projectInfo,projectRate,currentTerm);
        //保存债权还款明细
        repayDao.batchInsertCreditRepayDetail(actualRepayDetailList);
        //保存还款订单扩充信息
        projectRepayInfo.setProjectId(projectId);
        projectRepayInfo.setTerm(currentTerm);
        projectRepayInfo.setOrderId(orderId);
        projectRepayInfo.setRepayType(type);
        projectRepayInfo.setBorrowerRepayAmount(borrowerTotalRepay);
        projectRepayInfo.setRepayProgress(XWRepayProgress.PLATFORM_PRETREATMENT_FINISH);
        projectRepayInfo.setCompensatory(SysCompensatory.parse(compensatory));
        repayDao.createProjectRepayInfo(projectRepayInfo);
        //插入一条是否发送信息记录
        repayDao.insertRepayMsgFlag(orderId);
        return actualRepayDetailList;

    }



    private BigDecimal getDEBXMonthPayTotal(BigDecimal total, BigDecimal monthRate, int terms) {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms);
        return total.multiply(monthRate).multiply(tmp).divide(tmp.subtract(new BigDecimal(1)), 2,
                BigDecimal.ROUND_HALF_UP);
    }



    @Override
    public List<XWRepaymentPlan> generatePrepaymentPlan(Integer projectId, XWProjectInfo projectInfo, XWProjectExtraInfo projectExtraInfo, XWProjectRate projectRate, Integer currentTerm, Date currentDate, boolean modifyDatabase, XWProjectPrepaymentConfig xwProjectPrepaymentConfig, SysRepayOperationType type)throws Exception{
        List<XWRepaymentPlan> list=new ArrayList<>();
        //本期到期应还
        Map<String,Object> prepaymentPlanParams=new HashMap<>();
        prepaymentPlanParams.put("projectId",projectId);
        prepaymentPlanParams.put("term",currentTerm);
        prepaymentPlanParams.put("repayState",RepaymentPlan_RepayState.WH);
        List<XWRepaymentPlan> originList= repayDao.getRepaymentPlan(prepaymentPlanParams);
        //修改还款计划
        List<XWRepaymentPlan> currentTermPrepaymentPlans=xWRepayInTransaction.modifyRepaymentPlan(projectId,originList,projectInfo,projectExtraInfo,projectRate,currentTerm,currentDate,modifyDatabase,xwProjectPrepaymentConfig,type);
        list.addAll(currentTermPrepaymentPlans);
        //剩余本金以及成交服务费还款计划
        Map<String,Object> otherTermPrincipalParams=new HashMap<>();
        otherTermPrincipalParams.put("projectId",projectId);
        otherTermPrincipalParams.put("feeTypeList",new int[]{SysTradeFeeCode.TZ_BJ,SysTradeFeeCode.CJFWF});
        otherTermPrincipalParams.put("repayState",RepaymentPlan_RepayState.WH);
        otherTermPrincipalParams.put("notEqualTerm",currentTerm);
        List<XWRepaymentPlan> otherTermPrincipals=repayDao.getRepaymentPlan(otherTermPrincipalParams);
        list.addAll(otherTermPrincipals);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void xinwangRepayFinish(String requestNo,XWTradeType tradeType)throws Exception{
        //结束新网请求
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);
        //更新债权还款进度
        Map<String,Object> updateupdateCreditRepayDetailParams=new HashMap<>();
        if(XWTradeType.REPAYMENT==tradeType||XWTradeType.COMPENSATORY==tradeType){
            updateupdateCreditRepayDetailParams.put("repayState", CreditToRepay_RepayState.XINWANG_REPAY_FINISH);
            updateupdateCreditRepayDetailParams.put("byRepayRequestNo",requestNo);

            updateupdateCreditRepayDetailParams.put("oldRepayState",CreditToRepay_RepayState.ACCEPTED);
        }
        else if(XWTradeType.MARKETING==tradeType){
            updateupdateCreditRepayDetailParams.put("marketingState", CreditToRepay_RepayState.XINWANG_REPAY_FINISH);
            updateupdateCreditRepayDetailParams.put("byMarketingRequestNo",requestNo);
            updateupdateCreditRepayDetailParams.put("oldMarketingState",CreditToRepay_RepayState.ACCEPTED);
        }
        repayDao.updateCreditRepayDetail(updateupdateCreditRepayDetailParams);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platfromRepay(Integer projectId,XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,List<String> repaySuccessRequestList)throws Exception{

        String lockKey = "platfromRepay"+projectInfo.getProjectNo();

        try {

            boolean lockSuccess = redisUtilService.acquireLock(lockKey,1000 * 60 * 5);
            if (!lockSuccess) {
                throw new XWTradeException("该标正在操作平台还款"+lockKey);
            }

            XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(projectId);
            Integer term=projectRepayInfo.getTerm();
            //整标还款订单
            Integer orderId=projectRepayInfo.getOrderId();
            String remark=String.format("还款：%s，标题：%s， 第 %s期",projectInfo.getProjectCode(),projectInfo.getProjectName(), term);

            List<XWCapitalFlow> t6102sToInsert = new ArrayList<>();
            Map<Integer, XWFundAccount> investorWLZHMap = new HashMap<>();
            Map<Integer, XWFundAccount> investorSDZHMap = new HashMap<>();
            //以投资人为单位聚合参与复投的回款金额
            Map<Integer,BigDecimal> investorRepayAmountMap=new HashMap<>();

            // 新网平台收入账户
            XWFundAccount platformIncomeWLZH = accountDao.getFundAccount(SysCommonConsts.PLATFORM_USER_ID, SysFundAccountType.XW_PLATFORM_INCOME_WLZH);

            String payerUserNo=projectInfo.getBorrowerPlatformUserNo();
            Integer payerUserId=projectInfo.getBorrowerUserId();
            //如果代偿，用担保人账号
            if(projectRepayInfo.getCompensatory()==SysCompensatory.TRUE){
                payerUserNo=projectInfo.getGuaranteePlatformUserNo();
                payerUserId=projectExtraInfo.getGuaranteeUserId();
            }

            //借款人资金账户
            XinwangAccount payerXinwangAccount=accountDao.getXinwangAccount(payerUserNo);
            String payerFundAccountType="XW_"+payerXinwangAccount.getUserRole().getCode()+"_SDZH";
            XWFundAccount borrowerSDZH = accountDao.getFundAccount(payerUserId, SysFundAccountType.parse(payerFundAccountType));

            for(String repayRequestNo:repaySuccessRequestList){
                SysCreditToRepay creditRepayDetail=repayDao.getCreditRepayDetailByRepayRequestNo(repayRequestNo);
                if(creditRepayDetail.getRepayState().equals(CreditToRepay_RepayState.PLATFORM_REPAY_FINISH)){
                    LOG.info("请求[{}]流水已经完成平台还款",repayRequestNo);
                    continue;
                }

                boolean belongToPlan=creditRepayDetail.getPlanId()!=null;
                //投资人资金账户
                XWFundAccount investorWLZH = investorWLZHMap.get(creditRepayDetail.getInvestorId());
                if (investorWLZH == null) {
                    investorWLZH = accountDao.getFundAccount(creditRepayDetail.getInvestorId(), SysFundAccountType.XW_INVESTOR_WLZH);
                    investorWLZHMap.put(creditRepayDetail.getInvestorId(), investorWLZH);
                }

                //将债权对应的还款计划改为已还
                Map<String,Object> updateRepaymentPlanParams=new HashMap<>();
                updateRepaymentPlanParams.put("creditId",creditRepayDetail.getCreditId());
                List<Integer> feeTypeList=new ArrayList<>();//需要update还款计划的交易类型

                //本金
                if(creditRepayDetail.getPrincipal().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.TZ_BJ);
                    //借款人支出
                    borrowerSDZH.setAmount(borrowerSDZH.getAmount().subtract(creditRepayDetail.getPrincipal()));
                    //此处不应该再做校验
                    /*if (borrowerSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                        throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(),borrowerSDZH.getUserId()));
                    }*/
                    //借款人流水
                    XWCapitalFlow t6102jkr = new XWCapitalFlow();
                    t6102jkr.setFundAccountId(borrowerSDZH.getId());
                    t6102jkr.setTadeType(SysTradeFeeCode.TZ_BJ);
                    t6102jkr.setOtherFundAccountId(investorWLZH.getId());
                    t6102jkr.setExpenditure(creditRepayDetail.getPrincipal());
                    t6102jkr.setBalance(borrowerSDZH.getAmount());
                    t6102jkr.setRemark(remark);
                    t6102jkr.setProjectId(projectId);
                    t6102jkr.setOrderId(orderId);
                    t6102sToInsert.add(t6102jkr);
                    //投资人收入
                    investorWLZH.setAmount(investorWLZH.getAmount().add(creditRepayDetail.getPrincipal()));
                    //投资人流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(investorWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.TZ_BJ);
                    t6102tzr.setOtherFundAccountId(borrowerSDZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getPrincipal());
                    t6102tzr.setBalance(investorWLZH.getAmount());
                    t6102tzr.setRemark(remark);
                    t6102tzr.setProjectId(projectId);
                    if(belongToPlan){
                        t6102tzr.setLevel(XWCapitalFlowLevel.XT);
                    }
                    t6102tzr.setOrderId(orderId);
                    t6102sToInsert.add(t6102tzr);

                }
                //利息
                if(creditRepayDetail.getInterest().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.TZ_LX);



                    //借款人支出
                    borrowerSDZH.setAmount(borrowerSDZH.getAmount().subtract(creditRepayDetail.getInterest()));
    //                if (borrowerSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
    //                    throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(),projectInfo.getBorrowerUserId()));
    //                }
                    //借款人流水
                    XWCapitalFlow t6102jkr = new XWCapitalFlow();
                    t6102jkr.setFundAccountId(borrowerSDZH.getId());
                    t6102jkr.setTadeType(SysTradeFeeCode.TZ_LX);
                    t6102jkr.setOtherFundAccountId(investorWLZH.getId());
                    t6102jkr.setExpenditure(creditRepayDetail.getInterest());
                    t6102jkr.setBalance(borrowerSDZH.getAmount());
                    t6102jkr.setRemark(remark);
                    t6102jkr.setProjectId(projectId);
                    t6102jkr.setOrderId(orderId);
                    t6102sToInsert.add(t6102jkr);
                    //投资人收入
                    investorWLZH.setAmount(investorWLZH.getAmount().add(creditRepayDetail.getInterest()));
                    //投资人流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(investorWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.TZ_LX);
                    t6102tzr.setOtherFundAccountId(borrowerSDZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getInterest().add(
                            creditRepayDetail.getInterestServiceFee()//造利息流水时金额需要加回利息管理费
                            )
                    );
                    t6102tzr.setBalance(investorWLZH.getAmount().add(
                            creditRepayDetail.getInterestServiceFee()//造利息流水时金额需要加回利息管理费
                            )
                    );
                    t6102tzr.setRemark(remark);
                    t6102tzr.setProjectId(projectId);
                    if(belongToPlan){
                        t6102tzr.setLevel(XWCapitalFlowLevel.XT);
                    }
                    t6102tzr.setOrderId(orderId);
                    t6102sToInsert.add(t6102tzr);

                }


                //利息管理费  借款人->投资者-->平台 等同于 借款人-->平台
                if(creditRepayDetail.getInterestServiceFee().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.LX_GLF);
                    //借款人-->投资者
                    this.addXWCapitalFlow(borrowerSDZH,investorWLZH,creditRepayDetail.getInterestServiceFee(),projectId,orderId,t6102sToInsert,remark,SysTradeFeeCode.LX_GLF,XWCapitalFlowLevel.XT);

                    //投资者-->平台
                    this.addXWCapitalFlow(investorWLZH,platformIncomeWLZH,creditRepayDetail.getInterestServiceFee(),projectId,orderId,t6102sToInsert,remark,SysTradeFeeCode.LX_GLF,XWCapitalFlowLevel.YH);


                }

                //逾期罚息
                if(creditRepayDetail.getOverduePenalty().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.TZ_FX);
                    //借款人支出
                    borrowerSDZH.setAmount(borrowerSDZH.getAmount().subtract(creditRepayDetail.getOverduePenalty()));
    //                if (borrowerSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
    //                    throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(),projectInfo.getBorrowerUserId()));
    //                }
                    //借款人流水
                    XWCapitalFlow t6102jkr = new XWCapitalFlow();
                    t6102jkr.setFundAccountId(borrowerSDZH.getId());
                    t6102jkr.setTadeType(SysTradeFeeCode.TZ_FX);
                    t6102jkr.setOtherFundAccountId(investorWLZH.getId());
                    t6102jkr.setExpenditure(creditRepayDetail.getOverduePenalty());
                    t6102jkr.setBalance(borrowerSDZH.getAmount());
                    t6102jkr.setRemark(remark);
                    t6102jkr.setProjectId(projectId);
                    t6102jkr.setOrderId(orderId);
                    t6102sToInsert.add(t6102jkr);
                    //投资人收入
                    investorWLZH.setAmount(investorWLZH.getAmount().add(creditRepayDetail.getOverduePenalty()));
                    //投资人流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(investorWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.TZ_FX);
                    t6102tzr.setOtherFundAccountId(borrowerSDZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getOverduePenalty());
                    t6102tzr.setBalance(investorWLZH.getAmount());
                    t6102tzr.setRemark(remark);
                    t6102tzr.setProjectId(projectId);
                    t6102tzr.setOrderId(orderId);
                    t6102sToInsert.add(t6102tzr);

                }
                //提前还款违约金
                if(creditRepayDetail.getPrepayPenalty().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.TZ_WYJ);
                    //借款人支出
                    borrowerSDZH.setAmount(borrowerSDZH.getAmount().subtract(creditRepayDetail.getPrepayPenalty()));
    //                if (borrowerSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
    //                    throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(),projectInfo.getBorrowerUserId()));
    //                }
                    //借款人流水
                    XWCapitalFlow t6102jkr = new XWCapitalFlow();
                    t6102jkr.setFundAccountId(borrowerSDZH.getId());
                    t6102jkr.setTadeType(SysTradeFeeCode.TZ_WYJ);
                    t6102jkr.setOtherFundAccountId(investorWLZH.getId());
                    t6102jkr.setExpenditure(creditRepayDetail.getPrepayPenalty());
                    t6102jkr.setBalance(borrowerSDZH.getAmount());
                    t6102jkr.setRemark(remark);
                    t6102jkr.setProjectId(projectId);
                    t6102jkr.setOrderId(orderId);
                    t6102sToInsert.add(t6102jkr);
                    //投资人收入
                    investorWLZH.setAmount(investorWLZH.getAmount().add(creditRepayDetail.getPrepayPenalty()));
                    //投资人流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(investorWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.TZ_WYJ);
                    t6102tzr.setOtherFundAccountId(borrowerSDZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getPrepayPenalty());
                    t6102tzr.setBalance(investorWLZH.getAmount());
                    t6102tzr.setRemark(remark);
                    t6102tzr.setProjectId(projectId);
                    t6102tzr.setOrderId(orderId);
                    t6102sToInsert.add(t6102tzr);

                }
                //添加违约金分成流水 借款人->投资者-->平台 等同于 借款人-->平台
                if(creditRepayDetail.getPenaltyDivide().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.TZ_WYJ_FC);
                    this.addXWCapitalFlow(borrowerSDZH,investorWLZH,creditRepayDetail.getPenaltyDivide(),projectId,orderId,t6102sToInsert,remark,SysTradeFeeCode.TZ_WYJ_FC,XWCapitalFlowLevel.XT);

                    this.addXWCapitalFlow(investorWLZH,platformIncomeWLZH,creditRepayDetail.getPenaltyDivide(),projectId,orderId,t6102sToInsert,remark,SysTradeFeeCode.TZ_WYJ_FC,XWCapitalFlowLevel.XT);

                }

                //逾期手续费
                if(creditRepayDetail.getOverdueCommission().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.TZ_YQ_SXF);
                    //借款人支出
                    borrowerSDZH.setAmount(borrowerSDZH.getAmount().subtract(creditRepayDetail.getOverdueCommission()));
    //                if (borrowerSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
    //                    throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(),projectInfo.getBorrowerUserId()));
    //                }
                    //借款人流水
                    XWCapitalFlow t6102jkr = new XWCapitalFlow();
                    t6102jkr.setFundAccountId(borrowerSDZH.getId());
                    t6102jkr.setTadeType(SysTradeFeeCode.TZ_YQ_SXF);
                    t6102jkr.setOtherFundAccountId(investorWLZH.getId());
                    t6102jkr.setExpenditure(creditRepayDetail.getOverdueCommission());
                    t6102jkr.setBalance(borrowerSDZH.getAmount());
                    t6102jkr.setRemark(remark);
                    t6102jkr.setProjectId(projectId);
                    t6102jkr.setOrderId(orderId);
                    t6102sToInsert.add(t6102jkr);
                    //平台收入
                    platformIncomeWLZH.setAmount(platformIncomeWLZH.getAmount().add(creditRepayDetail.getOverdueCommission()));
                    //平台流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(platformIncomeWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.TZ_YQ_SXF);
                    t6102tzr.setOtherFundAccountId(borrowerSDZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getOverdueCommission());
                    t6102tzr.setBalance(platformIncomeWLZH.getAmount());
                    t6102tzr.setRemark(remark);
                    t6102tzr.setProjectId(projectId);
                    t6102tzr.setOrderId(orderId);
                    t6102sToInsert.add(t6102tzr);
                }
                //平台服务费
                if(creditRepayDetail.getServiceCharge().compareTo(BigDecimal.ZERO)>0){

                    //借款人支出
                    borrowerSDZH.setAmount(borrowerSDZH.getAmount().subtract(creditRepayDetail.getServiceCharge()));
    //                if (borrowerSDZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
    //                    throw new XWTradeException(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_SDZH_ACCOUNT_BALANCE_LACK.getMessage(),projectInfo.getBorrowerUserId()));
    //                }
                    //借款人流水
                    XWCapitalFlow t6102jkr = new XWCapitalFlow();
                    t6102jkr.setFundAccountId(borrowerSDZH.getId());
                    t6102jkr.setTadeType(SysTradeFeeCode.PTFWF);
                    t6102jkr.setOtherFundAccountId(investorWLZH.getId());
                    t6102jkr.setExpenditure(creditRepayDetail.getServiceCharge());
                    t6102jkr.setBalance(borrowerSDZH.getAmount());
                    t6102jkr.setRemark(remark);
                    t6102jkr.setProjectId(projectId);
                    t6102jkr.setOrderId(orderId);
                    t6102sToInsert.add(t6102jkr);
                    //平台收入
                    platformIncomeWLZH.setAmount(platformIncomeWLZH.getAmount().add(creditRepayDetail.getServiceCharge()));
                    //平台流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(platformIncomeWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.PTFWF);
                    t6102tzr.setOtherFundAccountId(borrowerSDZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getServiceCharge());
                    t6102tzr.setBalance(platformIncomeWLZH.getAmount());
                    t6102tzr.setRemark(remark);
                    t6102tzr.setProjectId(projectId);
                    t6102tzr.setOrderId(orderId);
                    t6102sToInsert.add(t6102tzr);
                }

                //成交服务费
                if(creditRepayDetail.getDealFee().compareTo(BigDecimal.ZERO)>0){
                    feeTypeList.add(SysTradeFeeCode.CJFWF);
                    this.addXWCapitalFlow(borrowerSDZH,platformIncomeWLZH,creditRepayDetail.getDealFee(),projectId,orderId,t6102sToInsert,remark,SysTradeFeeCode.CJFWF,XWCapitalFlowLevel.YH);

                }

                //更新计划债权还款金额
                if(belongToPlan){
                    BigDecimal planCreditFundReturn=creditRepayDetail.getPrincipal().add(creditRepayDetail.getInterest());
                    Map<String,Object> userPlanCreditParams=new HashMap<>();
                    userPlanCreditParams.put("productId",creditRepayDetail.getCreditId());
                    userPlanCreditParams.put("returnAmount",planCreditFundReturn);
                    planDao.updateUserPlanCreditReturnAmount(userPlanCreditParams);
                    //以投资人为单位聚合回款金额
                    BigDecimal investorRepayAmount=investorRepayAmountMap.get(creditRepayDetail.getInvestorId());
                    if(investorRepayAmount==null){
                        investorRepayAmount=BigDecimal.ZERO;
                    }
                    investorRepayAmountMap.put(creditRepayDetail.getInvestorId(), investorRepayAmount.add(planCreditFundReturn));
                }



                updateRepaymentPlanParams.put("feeTypeList",feeTypeList);
                if(projectRepayInfo.getRepayType()==SysRepayOperationType.REPAY){
                    updateRepaymentPlanParams.put("term",projectRepayInfo.getTerm());
                }
                repayDao.finishRepaymentPlanOfCredit(updateRepaymentPlanParams);//将这些交易类型改为已还


                //将该债权金额为0的全改为已还
                Map map = new HashMap();
                map.put("creditId",creditRepayDetail.getCreditId());
                map.put("repayState","WH");
                map.put("amount",BigDecimal.ZERO);
                if(projectRepayInfo.getRepayType()==SysRepayOperationType.REPAY){
                    map.put("term",projectRepayInfo.getTerm());
                }
                List<XWRepaymentPlan> list = repayDao.getRepaymentPlan(map);
                if(!CollectionUtils.isEmpty(list)){
                    List<Integer> ids = new ArrayList<>();
                    for (XWRepaymentPlan plan:list) {
                        ids.add(plan.getId());
                    }
                    map.put("ids",ids);
                    map.put("newStatus","YH");
                    map.put("now",1);
                    repayDao.updateRepaymentPlanByIds(map);
                }

                //更新债权持有金额
                BigDecimal repayCreditAmount=BigDecimal.ZERO;
                if(projectRepayInfo.getRepayType()==SysRepayOperationType.REPAY){
                    Map<String,Object> getRepaymentPlanParams=new HashMap<>();
                    getRepaymentPlanParams.put("feeType",SysTradeFeeCode.TZ_BJ);
                    getRepaymentPlanParams.put("term",term);
                    getRepaymentPlanParams.put("creditId",creditRepayDetail.getCreditId());
                    XWRepaymentPlan principalRepaymentPlan=repayDao.getRepaymentPlanByUniqueKey(getRepaymentPlanParams);
                    repayCreditAmount=principalRepaymentPlan==null?BigDecimal.ZERO:principalRepaymentPlan.getAmount();
                }
                else{
                    Map<String,Object> remainPrincipalParams=new HashMap<>();
                    remainPrincipalParams.put("creditId",creditRepayDetail.getCreditId());
                    remainPrincipalParams.put("term",term);
                    repayCreditAmount=repayDao.getRemainPrincipalOfCredit(remainPrincipalParams);
                }
                if(repayCreditAmount.compareTo(BigDecimal.ZERO)>0){
                    Map<String,Object> updateCreditParams=new HashMap<>();
                    updateCreditParams.put("holdCreditAmountReduce",repayCreditAmount);
                    updateCreditParams.put("id",creditRepayDetail.getCreditId());
                    creditDao.updateCreditInfoById(updateCreditParams);
                }
                //更新债权还款状态
                Map<String,Object> updateCreditRepayDetailParams=new HashMap<>();
                updateCreditRepayDetailParams.put("repayState", CreditToRepay_RepayState.PLATFORM_REPAY_FINISH);
                updateCreditRepayDetailParams.put("id",creditRepayDetail.getId());
                repayDao.updateCreditRepayDetail(updateCreditRepayDetailParams);
            }
            //如果是计划标，将投资人回款金额转入复投
            String frozenRemark="标"+projectId+"回款转入复投";
            for(Map.Entry<Integer, BigDecimal> item : investorRepayAmountMap.entrySet()){
                XWFundAccount investorWLZH= investorWLZHMap.get(item.getKey());
                XWFundAccount investorSDZH= investorSDZHMap.get(item.getKey());
                if (investorSDZH == null) {
                    investorSDZH = accountDao.getFundAccount(item.getKey(), SysFundAccountType.XW_INVESTOR_SDZH);
                    investorSDZHMap.put(item.getKey(), investorSDZH);
                }
                BigDecimal investorRepayAmount= item.getValue();
                investorWLZH.setAmount(investorWLZH.getAmount().subtract(investorRepayAmount));
                XWCapitalFlow t6102investorWLZH = new XWCapitalFlow();
                t6102investorWLZH.setFundAccountId(investorWLZH.getId());
                t6102investorWLZH.setTadeType(SysTradeFeeCode.PLAN_BID_REPAY_LOCK);
                t6102investorWLZH.setOtherFundAccountId(investorSDZH.getId());
                t6102investorWLZH.setExpenditure(investorRepayAmount);
                t6102investorWLZH.setBalance(investorWLZH.getAmount());
                t6102investorWLZH.setRemark(frozenRemark);
                t6102investorWLZH.setProjectId(projectId);
                t6102investorWLZH.setLevel(XWCapitalFlowLevel.XT);
                t6102investorWLZH.setOrderId(orderId);
                t6102sToInsert.add(t6102investorWLZH);

                investorSDZH.setAmount(investorSDZH.getAmount().add(investorRepayAmount));
                XWCapitalFlow t6102investorSDZH = new XWCapitalFlow();
                t6102investorSDZH.setFundAccountId(investorSDZH.getId());
                t6102investorSDZH.setTadeType(SysTradeFeeCode.PLAN_BID_REPAY_LOCK);
                t6102investorSDZH.setOtherFundAccountId(investorWLZH.getId());
                t6102investorSDZH.setIncome(investorRepayAmount);
                t6102investorSDZH.setBalance(investorSDZH.getAmount());
                t6102investorSDZH.setRemark(frozenRemark);
                t6102investorSDZH.setProjectId(projectId);
                t6102investorSDZH.setLevel(XWCapitalFlowLevel.XT);
                t6102investorSDZH.setOrderId(orderId);
                t6102sToInsert.add(t6102investorSDZH);
            }
            //更新账户余额
            List<XWFundAccount> t6101sToUpdate = new ArrayList<>();
            t6101sToUpdate.add(borrowerSDZH);
            t6101sToUpdate.add(platformIncomeWLZH);
            for(XWFundAccount fundAccount:investorWLZHMap.values()){
                t6101sToUpdate.add(fundAccount);
            }
            for(XWFundAccount fundAccount:investorSDZHMap.values()){
                t6101sToUpdate.add(fundAccount);
            }


            for(XWFundAccount fundAccount:t6101sToUpdate){
                Map<String,Object> updateFundAccountParams=new HashMap<>();
                updateFundAccountParams.put("id",fundAccount.getId());
                updateFundAccountParams.put("amount",fundAccount.getAmount());
                accountDao.updateFundAccount(updateFundAccountParams);
            }
            //插入流水
            commonDao.batchInsertT6102(t6102sToInsert);
            //当期是否还完
            ifCurrentTermRepayFinish(projectId,term,orderId);//

        } catch (Exception e) {
            throw e;//释放锁之后把异常抛回出来
        }finally {
            try {
                redisUtilService.releaseLock(lockKey);
            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.info("释放锁时失败【{}】",lockKey);
            }
        }


    }

    @Override
    public void ifCurrentTermRepayFinish(Integer projectId,Integer term,Integer orderId)throws Exception{
        XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(projectId);
        Map<String,Object> getCurrentTermNotYetRepayParams=new HashMap<>();
        getCurrentTermNotYetRepayParams.put("projectId",projectId);
        getCurrentTermNotYetRepayParams.put("term",term);
        getCurrentTermNotYetRepayParams.put("repayState",RepaymentPlan_RepayState.WH);
        List<XWRepaymentPlan> currentTermNotYetRepayList= repayDao.getRepaymentPlan(getCurrentTermNotYetRepayParams);
        if(currentTermNotYetRepayList.isEmpty()){
            Map<String,Object> updateProjectExtraInfoParams=new HashMap<>();
            Map<String,Object> getNextTermRepaymentPlanParams=new HashMap<>();
            getNextTermRepaymentPlanParams.put("projectId",projectId);
            getNextTermRepaymentPlanParams.put("term",term+1);
            List<XWRepaymentPlan> nextTermRepayList= repayDao.getRepaymentPlan(getNextTermRepaymentPlanParams);
            if(!nextTermRepayList.isEmpty()){
                updateProjectExtraInfoParams.put("nextRepayDate",nextTermRepayList.get(0).getDueDate());
                updateProjectExtraInfoParams.put("overdue", ProjectExtraInfo_Overdue.F);
            }
            int temp = projectExtraInfo.getRemainTerms().equals(0) ? 0 : projectExtraInfo.getRemainTerms() - 1;
            updateProjectExtraInfoParams.put("remainTerms", temp);
            updateProjectExtraInfoParams.put("id", projectId);
            projectDao.updateProjectExtraInfo(updateProjectExtraInfoParams);
            //更新标状态为非正在操作还款
            Map<String,Object> projectInfoParams=new HashMap<>();
            projectInfoParams.put("id",projectId);
            projectInfoParams.put("inProgressOfRepay",false);
            projectDao.updateProjectInfo(projectInfoParams);
            //结束整标还款订单
            orderService.success(orderId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void projectRepayFinish(Integer projectId)throws Exception{
        //当前时间
        Date currentDate=commonDao.getCurrentDate();
        //更新标状态为已结清
        Map<String,Object> projectInfoParams=new HashMap<>();
        projectInfoParams.put("id",projectId);
        projectInfoParams.put("state", PTProjectState.YJQ);
        projectDao.updateProjectInfo(projectInfoParams);
        //填入结清时间
        Map<String,Object> updateProjectExtraInfoParams=new HashMap<>();
        updateProjectExtraInfoParams.put("settleTime", currentDate);
        updateProjectExtraInfoParams.put("id", projectId);
        projectDao.updateProjectExtraInfo(updateProjectExtraInfoParams);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platfromMarketing(Integer projectId,XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,List<String> marketingSuccessRequestList)throws Exception{

        String lockKey = "platfromMarketing"+projectInfo.getProjectNo();
        try {
            boolean lockSuccess = redisUtilService.acquireLock(lockKey,1000 * 60 * 5);
            if (!lockSuccess) {
                throw new XWTradeException("该标正在操作平台还款"+lockKey);
            }

            Integer term=projectRepayInfo.getTerm();
            //整标还款订单
            Integer orderId=projectRepayInfo.getOrderId();
            String remark=String.format("散标还款：%s，标题：%s， 第 %s期",projectInfo.getProjectCode(),projectInfo.getProjectName(), term);
            List<XWCapitalFlow> t6102sToInsert = new ArrayList<>();
            Map<Integer, XWFundAccount> investorWLZHMap = new HashMap<>();
            Map<Integer, XWFundAccount> investorSDZHMap = new HashMap<>();
            //以投资人为单位聚合参与复投的回款金额
            Map<Integer,BigDecimal> investorRepayAmountMap=new HashMap<>();
            // 新网平台营销账户
            XWFundAccount platformMarketingWLZH = accountDao.getFundAccount(SysCommonConsts.PLATFORM_USER_ID, SysFundAccountType.XW_PLATFORM_MARKETING_WLZH);
            for(String repayRequestNo:marketingSuccessRequestList) {
                SysCreditToRepay creditRepayDetail = repayDao.getCreditRepayDetailByMarketingRequestNo(repayRequestNo);

                if(creditRepayDetail.getMarketingState().equals(CreditToRepay_RepayState.PLATFORM_REPAY_FINISH)){
                    LOG.info("请求[{}]流水已经完成平台营销还款",repayRequestNo);
                    continue;
                }

                //暂时计划中的标不会有加息，所以永远不会执行计划所匹配标加息对应的代码
                if (creditRepayDetail == null) {
                    continue;
                }
    //            boolean belongToPlan = creditRepayDetail == null ? false : creditRepayDetail.getPlanId() != null;
                //投资人资金账户
                XWFundAccount investorWLZH = investorWLZHMap.get(creditRepayDetail.getInvestorId());
                if (investorWLZH == null) {
                    investorWLZH = accountDao.getFundAccount(creditRepayDetail.getInvestorId(), SysFundAccountType.XW_INVESTOR_WLZH);
                    investorWLZHMap.put(creditRepayDetail.getInvestorId(), investorWLZH);
                }
                //标加息
                if(creditRepayDetail.getProjectIncreaseInterest().compareTo(BigDecimal.ZERO)>0){
                    //平台支出
                    platformMarketingWLZH.setAmount(platformMarketingWLZH.getAmount().subtract(creditRepayDetail.getProjectIncreaseInterest()));
    //                if (platformMarketingWLZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
    //                    throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getMessage(),SysCommonConsts.PLATFORM_USER_ID));
    //                }
                    //平台流水
                    XWCapitalFlow t6102pt = new XWCapitalFlow();
                    t6102pt.setFundAccountId(platformMarketingWLZH.getId());
                    t6102pt.setTadeType(SysTradeFeeCode.BID_JX);
                    t6102pt.setOtherFundAccountId(investorWLZH.getId());
                    t6102pt.setExpenditure(creditRepayDetail.getProjectIncreaseInterest());
                    t6102pt.setBalance(platformMarketingWLZH.getAmount());
                    t6102pt.setRemark(remark);
                    t6102pt.setProjectId(projectId);
                    t6102pt.setOrderId(orderId);
                    t6102sToInsert.add(t6102pt);
                    //投资人收入
                    investorWLZH.setAmount(investorWLZH.getAmount().add(creditRepayDetail.getProjectIncreaseInterest()));
                    //投资人流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(investorWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.BID_JX);
                    t6102tzr.setOtherFundAccountId(platformMarketingWLZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getProjectIncreaseInterest());
                    t6102tzr.setBalance(investorWLZH.getAmount());
                    t6102tzr.setRemark(remark);
                    t6102tzr.setProjectId(projectId);
                    t6102tzr.setOrderId(orderId);
                    t6102sToInsert.add(t6102tzr);
                }
                //投资加息
                if(creditRepayDetail.getTenderIncreaseInterest().compareTo(BigDecimal.ZERO)>0){
                    //平台支出
                    platformMarketingWLZH.setAmount(platformMarketingWLZH.getAmount().subtract(creditRepayDetail.getTenderIncreaseInterest()));
                    //这个是新网发放营销款之后才会做平台数据的修改，此处不应该再校验余额
                    /*if (platformMarketingWLZH.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                        throw new XWTradeException(XWResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getCode(),String.format(XWResponseCode.USER_WLZH_ACCOUNT_BALANCE_LACK.getMessage(),SysCommonConsts.PLATFORM_USER_ID));
                    }*/
                    //平台流水
                    XWCapitalFlow t6102pt = new XWCapitalFlow();
                    t6102pt.setFundAccountId(platformMarketingWLZH.getId());
                    t6102pt.setTadeType(SysTradeFeeCode.TZ_JX);
                    t6102pt.setOtherFundAccountId(investorWLZH.getId());
                    t6102pt.setExpenditure(creditRepayDetail.getTenderIncreaseInterest());
                    t6102pt.setBalance(platformMarketingWLZH.getAmount());
                    t6102pt.setRemark(remark);
                    t6102pt.setProjectId(projectId);
                    t6102pt.setOrderId(orderId);
                    t6102sToInsert.add(t6102pt);
                    //投资人收入
                    investorWLZH.setAmount(investorWLZH.getAmount().add(creditRepayDetail.getTenderIncreaseInterest()));
                    //投资人流水
                    XWCapitalFlow t6102tzr = new XWCapitalFlow();
                    t6102tzr.setFundAccountId(investorWLZH.getId());
                    t6102tzr.setTadeType(SysTradeFeeCode.TZ_JX);
                    t6102tzr.setOtherFundAccountId(platformMarketingWLZH.getId());
                    t6102tzr.setIncome(creditRepayDetail.getTenderIncreaseInterest());
                    t6102tzr.setBalance(investorWLZH.getAmount());
                    t6102tzr.setRemark(remark);
                    t6102tzr.setOrderId(orderId);
                    t6102tzr.setProjectId(projectId);
    //                if(belongToPlan){
    //                    t6102tzr.setLevel(XWCapitalFlowLevel.XT);
    //                }
                    t6102sToInsert.add(t6102tzr);
                }
                //更新计划债权还款金额
    //            if(belongToPlan){
    //                BigDecimal planCreditFundReturn=creditRepayDetail.getTenderIncreaseInterest();
    //                Map<String,Object> userPlanCreditParams=new HashMap<>();
    //                userPlanCreditParams.put("productId",creditRepayDetail.getCreditId());
    //                userPlanCreditParams.put("returnAmount",planCreditFundReturn);
    //                planDao.updateUserPlanCreditReturnAmount(userPlanCreditParams);
    //                //以投资人为单位聚合回款金额
    //                BigDecimal investorRepayAmount=investorRepayAmountMap.get(creditRepayDetail.getInvestorId());
    //                if(investorRepayAmount==null){
    //                    investorRepayAmount=BigDecimal.ZERO;
    //                }
    //                investorRepayAmountMap.put(creditRepayDetail.getInvestorId(), investorRepayAmount.add(planCreditFundReturn));
    //            }
                //将债权对应的还款计划改为已还
                Map<String,Object> updateRepaymentPlanParams=new HashMap<>();
                updateRepaymentPlanParams.put("creditId",creditRepayDetail.getCreditId());
                List<Integer> feeTypeList=new ArrayList<>();
                feeTypeList.add(SysTradeFeeCode.TZ_JX);
                feeTypeList.add(SysTradeFeeCode.BID_JX);
                updateRepaymentPlanParams.put("feeTypeList",feeTypeList);
                if(projectRepayInfo.getRepayType()==SysRepayOperationType.REPAY){
                    updateRepaymentPlanParams.put("term",projectRepayInfo.getTerm());
                }
                repayDao.finishRepaymentPlanOfCredit(updateRepaymentPlanParams);
                //更新债权加息还款状态
                Map<String,Object> updateCreditRepayDetailParams=new HashMap<>();
                updateCreditRepayDetailParams.put("marketingState", CreditToRepay_RepayState.PLATFORM_REPAY_FINISH);
                updateCreditRepayDetailParams.put("id",creditRepayDetail.getId());
                repayDao.updateCreditRepayDetail(updateCreditRepayDetailParams);
            }
            //如果是计划标，将投资人回款金额转入复投
            String frozenRemark="标"+projectId+"回款转入复投";
            for(Map.Entry<Integer, BigDecimal> item : investorRepayAmountMap.entrySet()){
                XWFundAccount investorWLZH= investorWLZHMap.get(item.getKey());
                XWFundAccount investorSDZH= investorSDZHMap.get(item.getKey());
                if (investorSDZH == null) {
                    investorSDZH = accountDao.getFundAccount(item.getKey(), SysFundAccountType.XW_INVESTOR_SDZH);
                    investorSDZHMap.put(item.getKey(), investorSDZH);
                }
                BigDecimal investorRepayAmount= item.getValue();
                investorWLZH.setAmount(investorWLZH.getAmount().subtract(investorRepayAmount));
                XWCapitalFlow t6102investorWLZH = new XWCapitalFlow();
                t6102investorWLZH.setFundAccountId(investorWLZH.getId());
                t6102investorWLZH.setTadeType(SysTradeFeeCode.PLAN_BID_REPAY_LOCK);
                t6102investorWLZH.setOtherFundAccountId(investorSDZH.getId());
                t6102investorWLZH.setExpenditure(investorRepayAmount);
                t6102investorWLZH.setBalance(investorWLZH.getAmount());
                t6102investorWLZH.setRemark(frozenRemark);
                t6102investorWLZH.setProjectId(projectId);
                t6102investorWLZH.setLevel(XWCapitalFlowLevel.XT);
                t6102investorWLZH.setOrderId(orderId);
                t6102sToInsert.add(t6102investorWLZH);

                investorSDZH.setAmount(investorSDZH.getAmount().add(investorRepayAmount));
                XWCapitalFlow t6102investorSDZH = new XWCapitalFlow();
                t6102investorSDZH.setFundAccountId(investorSDZH.getId());
                t6102investorSDZH.setTadeType(SysTradeFeeCode.PLAN_BID_REPAY_LOCK);
                t6102investorSDZH.setOtherFundAccountId(investorWLZH.getId());
                t6102investorSDZH.setIncome(investorRepayAmount);
                t6102investorSDZH.setBalance(investorSDZH.getAmount());
                t6102investorSDZH.setRemark(frozenRemark);
                t6102investorSDZH.setProjectId(projectId);
                t6102investorSDZH.setLevel(XWCapitalFlowLevel.XT);
                t6102investorSDZH.setOrderId(orderId);
                t6102sToInsert.add(t6102investorSDZH);
            }
            //更新账户余额
            List<XWFundAccount> t6101sToUpdate = new ArrayList<>();
            t6101sToUpdate.add(platformMarketingWLZH);
            for(XWFundAccount fundAccount:investorWLZHMap.values()){
                t6101sToUpdate.add(fundAccount);
            }
            for(XWFundAccount fundAccount:investorSDZHMap.values()){
                t6101sToUpdate.add(fundAccount);
            }
            for(XWFundAccount fundAccount:t6101sToUpdate){
                Map<String,Object> updateFundAccountParams=new HashMap<>();
                updateFundAccountParams.put("id",fundAccount.getId());
                updateFundAccountParams.put("amount",fundAccount.getAmount());
                if(fundAccount.getAmount().compareTo(BigDecimal.ZERO)<0){
                    LOG.info("------------营销账户余额不足-------------");
                    throw new RuntimeException("营销账户余额不足，请充钱");
                }
                accountDao.updateFundAccount(updateFundAccountParams);
            }
            //插入流水
            if (t6102sToInsert.size() > 0) {
                commonDao.batchInsertT6102(t6102sToInsert);
            }
            //当期是否还完
            ifCurrentTermRepayFinish(projectId,term,orderId);


        } catch (Exception e) {
            throw e;//释放锁之后把异常抛回出来
        }finally {
            try {
                redisUtilService.releaseLock(lockKey);
            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.info("释放锁时失败【{}】",lockKey);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendLetterAndMsg(Integer projectId,XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo) throws Exception{

        Integer repayMsgFlag=repayDao.getRepayMsgFlagByOrderId(projectRepayInfo.getOrderId());
        if(repayMsgFlag!=0){
            return;//已经发送过短信
        }


        Map<String,Object> getCurrentTermNotYetRepayParams=new HashMap<>();
        getCurrentTermNotYetRepayParams.put("projectId",projectId);
        getCurrentTermNotYetRepayParams.put("term",projectRepayInfo.getTerm());
        getCurrentTermNotYetRepayParams.put("repayState",RepaymentPlan_RepayState.WH);
        List<XWRepaymentPlan> currentTermNotYetRepayList= repayDao.getRepaymentPlan(getCurrentTermNotYetRepayParams);
        //当期还完
        if(currentTermNotYetRepayList.isEmpty()){
            List<Integer> sealBidUsers=repayDao.getSealBidUsers();
            Integer totalTerms=projectInfo.getMonthProjectPeriod()==0?1:projectInfo.getMonthProjectPeriod();
            Integer planId=repayDao.planHasProjectNotYetRepay(projectId);
            List<SysCreditToRepay> creditRepayDetailList=repayDao.getCreditRepayDetailByOrderId(projectRepayInfo.getOrderId());




            //不属于计划
            if(planId==null){
                for(SysCreditToRepay creditRepayDetail:creditRepayDetailList){
                    //该债权不属于计划，而且不是封标用户
                    if(creditRepayDetail.getPlanId()==null&&!sealBidUsers.contains(creditRepayDetail.getInvestorId())){
                        PlatformAccount platformAccount=accountDao.getPlatformAccountInfoByUserId(creditRepayDetail.getInvestorId());
                        //正常还款
                        if(projectRepayInfo.getRepayType()==SysRepayOperationType.REPAY){
                            BigDecimal principal=creditRepayDetail.getPrincipal();
                            BigDecimal interest=creditRepayDetail.getInterest();
                            BigDecimal overduePenalty=creditRepayDetail.getOverduePenalty();
                            BigDecimal increaseInterest=creditRepayDetail.getTenderIncreaseInterest().add(creditRepayDetail.getProjectIncreaseInterest());
                            BigDecimal creditTotalRepayAmount=principal.add(interest).add(overduePenalty).add(increaseInterest);
                            //站内信
                            String repayInvestorLetter=ptCommonDao.getSystemVariable(SysVariableConsts.REPAY_INVESTOR_LETTER);
                            repayInvestorLetter=repayInvestorLetter.replace("${name}",projectInfo.getProjectName());
                            repayInvestorLetter=repayInvestorLetter.replace("${term}","第" + projectRepayInfo.getTerm() + "/" + totalTerms + "期");
                            repayInvestorLetter=repayInvestorLetter.replace("${amount}", StringHelper.formatAmount(creditTotalRepayAmount));
                            repayInvestorLetter=repayInvestorLetter.replace("${principal}", StringHelper.formatAmount(principal));
                            repayInvestorLetter=repayInvestorLetter.replace("${interest}", StringHelper.formatAmount(interest));
                            repayInvestorLetter=repayInvestorLetter.replace("${raiseInterest}", "，加息奖励" +StringHelper.formatAmount(increaseInterest)+ "元");
                            repayInvestorLetter=repayInvestorLetter.replace("${latecharge}", "，其他利息" +StringHelper.formatAmount(overduePenalty)+ "元");
                            ptCommonService.sendLetter(creditRepayDetail.getInvestorId(), "回款通知", repayInvestorLetter);
                            //短信
                            String repayInvestorMsg=ptCommonDao.getSystemVariable(SysVariableConsts.REPAY_INVESTOR_MSG);
                            repayInvestorMsg=repayInvestorMsg.replace("${name}",projectInfo.getProjectName());
                            repayInvestorMsg=repayInvestorMsg.replace("${term}","第" + projectRepayInfo.getTerm() + "/" + totalTerms + "期");
                            repayInvestorMsg=repayInvestorMsg.replace("${amount}", StringHelper.formatAmount(creditTotalRepayAmount));
                            repayInvestorMsg=repayInvestorMsg.replace("${principal}", StringHelper.formatAmount(principal));
                            repayInvestorMsg=repayInvestorMsg.replace("${interest}", StringHelper.formatAmount(interest));
                            repayInvestorMsg=repayInvestorMsg.replace("${raiseInterest}", "，加息奖励" +StringHelper.formatAmount(increaseInterest)+ "元");
                            repayInvestorMsg=repayInvestorMsg.replace("${latecharge}", "，其他利息" +StringHelper.formatAmount(overduePenalty)+ "元");
                            ptCommonService.sendMsg(platformAccount.getMobile(),repayInvestorMsg, SysMsgSendType.ACTIVE.getValue());
                        }
                        //提前还款
                        else{
                            BigDecimal principal=creditRepayDetail.getPrincipal();
                            BigDecimal interest=creditRepayDetail.getInterest();
                            BigDecimal prepayPenalty=creditRepayDetail.getPrepayPenalty();
                            BigDecimal increaseInterest=creditRepayDetail.getTenderIncreaseInterest().add(creditRepayDetail.getProjectIncreaseInterest());
                            BigDecimal creditTotalPrepayAmount=principal.add(interest).add(prepayPenalty).add(increaseInterest);
                            //站内信
                            String prepayInvestorLetter=ptCommonDao.getSystemVariable(SysVariableConsts.PREPAY_INVESTOR_LETTER);
                            prepayInvestorLetter=prepayInvestorLetter.replace("${name}",projectInfo.getProjectName());
                            prepayInvestorLetter=prepayInvestorLetter.replace("${amount}",StringHelper.formatAmount(creditTotalPrepayAmount));
                            prepayInvestorLetter=prepayInvestorLetter.replace("${principal}",StringHelper.formatAmount(principal));
                            prepayInvestorLetter=prepayInvestorLetter.replace("${interest}",StringHelper.formatAmount(interest));
                            prepayInvestorLetter=prepayInvestorLetter.replace("${penalty}","，其他利息" + StringHelper.formatAmount(prepayPenalty) + "元");
                            prepayInvestorLetter=prepayInvestorLetter.replace("${raiseInterest}","，加息奖励" + StringHelper.formatAmount(increaseInterest) + "元");
                            ptCommonService.sendLetter(creditRepayDetail.getInvestorId(), "回款通知", prepayInvestorLetter);
                            //短信
                            String prepayInvestorMsg=ptCommonDao.getSystemVariable(SysVariableConsts.PREPAY_INVESTOR_MSG);
                            prepayInvestorMsg=prepayInvestorMsg.replace("${name}",projectInfo.getProjectName());
                            prepayInvestorMsg=prepayInvestorMsg.replace("${amount}",StringHelper.formatAmount(creditTotalPrepayAmount));
                            prepayInvestorMsg=prepayInvestorMsg.replace("${principal}",StringHelper.formatAmount(principal));
                            prepayInvestorMsg=prepayInvestorMsg.replace("${interest}",StringHelper.formatAmount(interest));
                            prepayInvestorMsg=prepayInvestorMsg.replace("${penalty}","，其他利息" + StringHelper.formatAmount(prepayPenalty) + "元");
                            prepayInvestorMsg=prepayInvestorMsg.replace("${raiseInterest}","，加息奖励" + StringHelper.formatAmount(increaseInterest) + "元");
                            ptCommonService.sendMsg(platformAccount.getMobile(),prepayInvestorMsg,SysMsgSendType.ACTIVE.getValue());
                        }
                    }
                }
            }
            //该计划的标全部已结清
            else{
                SysPlan plan = planDao.getPlanById(planId);
                Map<Integer,BigDecimal> principalMap=new HashMap<>();
                Map<Integer,BigDecimal> interestMap=new HashMap<>();
                Map<Integer,BigDecimal> overduePenaltyMap=new HashMap<>();
                Map<Integer,BigDecimal> tenderIncreaseInterestMap=new HashMap<>();
                Map<Integer,BigDecimal> projectIncreaseInterestMap=new HashMap<>();
                List<SysPlanPayeeReceivePayment> planPayeeReceivePaymentList=planDao.sumBidPlanData(planId);
                for(SysPlanPayeeReceivePayment item:planPayeeReceivePaymentList){
                    if(SysTradeFeeCode.TZ_BJ==item.getFeeType()){
                        principalMap.put(item.getPayeeId(),item.getAmount());
                    }
                    else if(SysTradeFeeCode.TZ_LX==item.getFeeType()){
                        interestMap.put(item.getPayeeId(),item.getAmount());
                    }
                    else if(SysTradeFeeCode.TZ_FX==item.getFeeType()){
                        overduePenaltyMap.put(item.getPayeeId(),item.getAmount());
                    }
                    else if(SysTradeFeeCode.TZ_JX==item.getFeeType()){
                        tenderIncreaseInterestMap.put(item.getPayeeId(),item.getAmount());
                    }
                    else if(SysTradeFeeCode.BID_JX==item.getFeeType()){
                        projectIncreaseInterestMap.put(item.getPayeeId(),item.getAmount());
                    }
                }
                if(principalMap!=null){
                    for(Integer investorId:principalMap.keySet()){
                        if(!sealBidUsers.contains(investorId)){
                            BigDecimal principal=principalMap.get(investorId);
                            principal=principal==null?BigDecimal.ZERO:principal;
                            BigDecimal interest=BigDecimal.ZERO;
                            if(interestMap!=null){
                                interest=interestMap.get(investorId);
                                interest=interest==null?BigDecimal.ZERO:interest;
                            }
                            BigDecimal overduePenalty=BigDecimal.ZERO;
                            if(overduePenaltyMap!=null){
                                overduePenalty=overduePenaltyMap.get(investorId);
                                overduePenalty=overduePenalty==null?BigDecimal.ZERO:overduePenalty;
                            }
                            BigDecimal tenderIncreaseInterest=BigDecimal.ZERO;
                            if(tenderIncreaseInterestMap!=null){
                                tenderIncreaseInterest=tenderIncreaseInterestMap.get(investorId);
                                tenderIncreaseInterest=tenderIncreaseInterest==null?BigDecimal.ZERO:tenderIncreaseInterest;
                            }
                            BigDecimal projectIncreaseInterest=BigDecimal.ZERO;
                            if(projectIncreaseInterestMap!=null){
                                projectIncreaseInterest=projectIncreaseInterestMap.get(investorId);
                                projectIncreaseInterest=projectIncreaseInterest==null?BigDecimal.ZERO:projectIncreaseInterest;
                            }
                            BigDecimal earnings=interest.add(overduePenalty).add(tenderIncreaseInterest).add(projectIncreaseInterest);
                            // 站内信
                            String planRepayInvestorLetter=ptCommonDao.getSystemVariable(SysVariableConsts.PLAN_REPAY_INVESTOR_LETTER);
                            planRepayInvestorLetter=planRepayInvestorLetter.replace("${planName}",plan.getName());
                            planRepayInvestorLetter=planRepayInvestorLetter.replace("${principal}",StringHelper.formatAmount(principal));
                            planRepayInvestorLetter=planRepayInvestorLetter.replace("${earnings}",StringHelper.formatAmount(earnings));
                            ptCommonService.sendLetter(investorId,"计划回款通知",planRepayInvestorLetter);
                            // 短信
                            String planRepayInvestorMsg=ptCommonDao.getSystemVariable(SysVariableConsts.PLAN_REPAY_INVESTOR_MSG);
                            planRepayInvestorMsg=planRepayInvestorMsg.replace("${planName}",plan.getName());
                            planRepayInvestorMsg=planRepayInvestorMsg.replace("${principal}",StringHelper.formatAmount(principal));
                            planRepayInvestorMsg=planRepayInvestorMsg.replace("${earnings}",StringHelper.formatAmount(earnings));
                            PlatformAccount platformAccount= accountDao.getPlatformAccountInfoByUserId(investorId);
                            ptCommonService.sendMsg(platformAccount.getMobile(),planRepayInvestorMsg,SysMsgSendType.ACTIVE.getValue());
                        }
                    }
                }
            }


            //更新短信发送状态
            Map<String,Object> params=new HashMap<>();
            params.put("orderId",projectRepayInfo.getOrderId());
            params.put("flag",1);
            repayDao.updateRepayMsgFlagByOrderId(params);
        }
    }




    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveApplyInfo(XWProjectInfo projectInfo) {
        String lockKey = "lockRepayStatus"+projectInfo.getProjectNo();
        SystemOrder systemOrder = null;
        try {
            boolean lockSuccess = redisUtilService.acquireLock(lockKey,1000 * 60 * 2);
            if (!lockSuccess) {
                throw new XWTradeException(XWResponseCode.TRADE_REPAY_IN_PROGRESS_OF_REPAY);
            }
            //更新标状态为正在操作还款
            Map<String,Object> updateProjectInfoParams=new HashMap<>();
            updateProjectInfoParams.put("id",projectInfo.getProjectNo());
            updateProjectInfoParams.put("inProgressOfRepay",true);
            projectDao.updateProjectInfo(updateProjectInfoParams);
            //创建平台还款订单
            systemOrder = new SystemOrder();
            systemOrder.setTypeCode(XWTradeOrderType.PROJECT_REPAY.orderType());
            systemOrder.setOrderStatus(XWOrderStatus.DTJ);
            systemOrder.setSource(Source.HT);
            systemOrder.setUserId(projectInfo.getBorrowerUserId());
            orderManageDao.add(systemOrder);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;//释放锁之后抛回异常出来
        } finally {
            try {
                redisUtilService.releaseLock(lockKey);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.info("释放锁时失败【{}】",lockKey);
            }
        }

        return systemOrder.getId();
    }

    @Override
    public void addUnusualRepay(Integer orderId, Integer bidId) {
        UnusualRepay unusualRepay = repayDao.getUnusualRepayByOrder(orderId);
        if (unusualRepay != null) {
            unusualRepay.setStatus(true);
            repayDao.updateUnusualRepay(unusualRepay,false);
            return;
        }
        repayDao.addUnulsualRepay(new UnusualRepay(orderId, bidId, true));
    }

    @Override
    public void addXWCapitalFlow(XWFundAccount outAccount, XWFundAccount inAccount, BigDecimal amount, Integer projectId, Integer orderId, List<XWCapitalFlow> t6102sToInsert, String remark, int tradeType, XWCapitalFlowLevel level) {

        //资金支出
        outAccount.setAmount(outAccount.getAmount().subtract(amount));
        //资金收入
        inAccount.setAmount(inAccount.getAmount().add(amount));

        //支出流水
        XWCapitalFlow outFlow = new XWCapitalFlow();
        outFlow.setFundAccountId(outAccount.getId());
        outFlow.setTadeType(tradeType);
        outFlow.setOtherFundAccountId(inAccount.getId());
        outFlow.setExpenditure(amount);
        outFlow.setBalance(outAccount.getAmount());
        outFlow.setRemark(remark);
        outFlow.setProjectId(projectId);
        outFlow.setOrderId(orderId);
        outFlow.setLevel(level);
        t6102sToInsert.add(outFlow);
        //收入流水
        XWCapitalFlow inFlow = new XWCapitalFlow();
        inFlow.setFundAccountId(inAccount.getId());
        inFlow.setTadeType(tradeType);
        inFlow.setOtherFundAccountId(outAccount.getId());
        inFlow.setIncome(amount);
        inFlow.setBalance(inAccount.getAmount());
        inFlow.setRemark(remark);
        inFlow.setProjectId(projectId);
        inFlow.setOrderId(orderId);
        inFlow.setLevel(level);
        t6102sToInsert.add(inFlow);
    }
}
