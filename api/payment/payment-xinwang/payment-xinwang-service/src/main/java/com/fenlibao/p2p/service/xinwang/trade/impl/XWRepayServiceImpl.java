package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysCreditToRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentPlan_RepayState;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWRepayProgress;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.vo.RepayBudgetVO;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.project.XWChangeProjectStatusService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayService;
import com.fenlibao.p2p.service.xinwang.trade.XWRepayTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Administrator on 2017/6/13.
 */
@Service
public class XWRepayServiceImpl implements XWRepayService{

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
    XWRequestDao requestDao;

    @Resource
    SysOrderService orderService;

    @Resource
    XWChangeProjectStatusService changeProjectStatusService;

    @Resource
    XWRepayTransactionService repayTransactionService;

    @Override
    public RepayBudgetVO getRepayBudget(Integer projectId, SysRepayOperationType type,Boolean compensatory) throws Exception{
        //标信息
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
        if(projectInfo==null){
            throw new XWTradeException(XWResponseCode.BID_NOT_EXIST);
        }
        //标扩展信息
        XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(projectId);
        //标费率
        XWProjectRate projectRate=projectDao.getProjectRateById(projectId);
        //还款期号
        Integer currentTerm=repayDao.getCurrentTerm(projectId);
        //当前时间
        Date currentDate=commonDao.getCurrentDate();


//        currentDate =DateUtil.StrToDate("2018-03-19 20:00:00");//TODO:测试提前还款，记得删掉

        //还款计划
        List<XWRepaymentPlan> repaymentPlanList=null;
        //到期还款
        if(type==SysRepayOperationType.REPAY){
            Map<String,Object> repaymentPlanParams=new HashMap<>();
            repaymentPlanParams.put("projectId",projectId);
            repaymentPlanParams.put("term",currentTerm);
            repaymentPlanParams.put("repayState",RepaymentPlan_RepayState.WH);
            repaymentPlanList= repayDao.getRepaymentPlan(repaymentPlanParams);
        }
        //提前还款
        else{
            repaymentPlanList=repayTransactionService.generatePrepaymentPlan(projectId,projectInfo,projectExtraInfo,projectRate,currentTerm,currentDate,false, null,type);
        }

        RepayBudgetVO repayBudgetVO=new RepayBudgetVO();
        repayBudgetVO.serviceFee = projectInfo.getProjectAmount().multiply(projectRate.getTransactionServiceRate()).setScale(2, RoundingMode.HALF_UP);
        for(XWRepaymentPlan repaymentPlan:repaymentPlanList){
            if(repaymentPlan.getFeeType()==SysTradeFeeCode.TZ_BJ){
                repayBudgetVO.principal=repayBudgetVO.principal.add(repaymentPlan.getAmount());
            }
            else if(repaymentPlan.getFeeType()==SysTradeFeeCode.TZ_LX){
                repayBudgetVO.interest=repayBudgetVO.interest.add(repaymentPlan.getAmount());
            }
            else if(repaymentPlan.getFeeType()==SysTradeFeeCode.TZ_FX){
                repayBudgetVO.overduePenalty=repayBudgetVO.overduePenalty.add(repaymentPlan.getAmount());
            }
            else if(repaymentPlan.getFeeType()==SysTradeFeeCode.TZ_YQ_SXF){
                repayBudgetVO.overdueCommission=repayBudgetVO.overdueCommission.add(repaymentPlan.getAmount());
            }
            else if(repaymentPlan.getFeeType()==SysTradeFeeCode.TZ_WYJ){
                repayBudgetVO.prepayPenalty=repayBudgetVO.prepayPenalty.add(repaymentPlan.getAmount());
            }else if(repaymentPlan.getFeeType()==SysTradeFeeCode.CJFWF){
                repayBudgetVO.serviceFeeNotPay = repayBudgetVO.serviceFeeNotPay.add(repaymentPlan.getAmount());
            }else if(repaymentPlan.getFeeType()==SysTradeFeeCode.LX_GLF){
                repayBudgetVO.interestManagementFee=repayBudgetVO.interestManagementFee.add(repaymentPlan.getAmount());
            }
        }
        repayBudgetVO.projectName=projectInfo.getProjectName();

        repayBudgetVO.totalAmount=
                repayBudgetVO.principal
                        .add(repayBudgetVO.interest)
                        .add(repayBudgetVO.overduePenalty)
                        .add(repayBudgetVO.overdueCommission)
                        .add(repayBudgetVO.prepayPenalty)
                        .add(repayBudgetVO.serviceFeeNotPay)
                        .add(repayBudgetVO.interestManagementFee);
        if(projectInfo.getGuaranteePlatformUserNo()!=null){
            PlatformAccount platformAccount=accountDao.getPlatformAccountInfoByPlatformUserNo(projectInfo.getGuaranteePlatformUserNo());
            repayBudgetVO.guaranteeName=platformAccount.getUserName();
        }

        if(compensatory){
            if(projectInfo.getGuaranteePlatformUserNo()!=null) {
                XinwangAccount xinwangAccount = accountDao.getXinwangAccount(projectInfo.getGuaranteePlatformUserNo());
                XWFundAccount guaranteeFundAccount = accountDao.getFundAccount(projectExtraInfo.getGuaranteeUserId(), SysFundAccountType.parse("XW_" + xinwangAccount.getUserRole().name() + "_WLZH"));
                repayBudgetVO.balance = guaranteeFundAccount.getAmount();
            }
        }
        else{
            XinwangAccount xinwangAccount=accountDao.getXinwangAccount(projectInfo.getBorrowerPlatformUserNo());
            XWFundAccount borrowerFundAccount=accountDao.getFundAccount(projectInfo.getBorrowerUserId(),SysFundAccountType.parse("XW_"+xinwangAccount.getUserRole().name()+"_WLZH"));
            repayBudgetVO.balance=borrowerFundAccount.getAmount();
        }
        return repayBudgetVO;
    }



    @Override
    public void repayApply(Integer projectId, SysRepayOperationType type,Boolean compensatory,XWProjectPrepaymentConfig xwProjectPrepaymentConfig) throws Exception {
        //标信息
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
        if(projectInfo==null){
            throw new XWTradeException(XWResponseCode.BID_NOT_EXIST);
        }
        if(projectInfo.getState()!= PTProjectState.HKZ){
            throw new XWTradeException(XWResponseCode.TRADE_REPAY_CONDITIONS_NOT_SATISFIED);
        }
        //锁数据
        Integer orderId = repayTransactionService.saveApplyInfo(projectInfo);
        //当前时间
        Date currentDate=commonDao.getCurrentDate();

        //标还款订单扩展信息
        SysProjectRepayInfo projectRepayInfo=new SysProjectRepayInfo();
        //保存点一，用户分利宝账户冻结还款金额，修改还款计划
        List<SysCreditToRepay> actualRepayDetailList=null;
        try {
            //actualRepayDetailList 已经是flb.t_xw_credit_repay 保存的数据
            actualRepayDetailList = repayTransactionService.platformPretreatment(projectId, projectInfo, type, projectRepayInfo, orderId, compensatory, currentDate,xwProjectPrepaymentConfig);
        }
        catch(Exception e){
            //更新标状态为非正在操作还款
            Map<String,Object> updateProjectInfoParams=new HashMap<>();
            updateProjectInfoParams.put("id",projectId);
            updateProjectInfoParams.put("inProgressOfRepay",false);
            projectDao.updateProjectInfo(updateProjectInfoParams);
            //结束整标还款订单
            orderService.fail(orderId);
            throw e;
        }
        //保存点一之后 TODO:测试不走新网时注释afterSavePointOne
        afterSavePointOne(projectInfo,projectRepayInfo,orderId,actualRepayDetailList,compensatory);
    }

    @Override
    public void handleError(Integer orderId)throws Exception{
        SysProjectRepayInfo projectRepayInfo=repayDao.getProjectRepayInfoByOrderId(orderId);
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectRepayInfo.getProjectId());
        List<SysCreditToRepay> actualRepayDetailList=repayDao.getCreditRepayDetailByOrderId(orderId);
        Boolean compensatory=Boolean.valueOf(projectRepayInfo.getCompensatory().name());
        if(projectRepayInfo.getRepayProgress()==XWRepayProgress.PLATFORM_PRETREATMENT_FINISH){
            afterSavePointOne(projectInfo,projectRepayInfo,orderId,actualRepayDetailList,compensatory);
        }
        else if(projectRepayInfo.getRepayProgress()==XWRepayProgress.XINWANG_FUND_FROZEN){
            List<SysCreditToRepay> creditRepayAcceptFailList=repayDao.getCreditRepayAcceptFailList(orderId);
            afterSavePointTwo(projectInfo,projectRepayInfo,orderId,creditRepayAcceptFailList,compensatory,projectRepayInfo.getPreTreatRequestNo());
        }
        else if(projectRepayInfo.getRepayProgress()==XWRepayProgress.XINWANG_REPAY_REQUEST_ACCEPTED){
            List<SysCreditToRepay> creditMarketingAcceptFailList=repayDao.getCreditMarketingAcceptFailList(orderId);
            afterSavePointThree(projectInfo,projectRepayInfo,orderId,creditMarketingAcceptFailList);
        }
        else if(projectRepayInfo.getRepayProgress()==XWRepayProgress.XINWANG_MARKETING_REQUEST_ACCEPTED){
            handleErrorDuringNotify(projectRepayInfo,projectInfo);
        }
    }

    public void afterSavePointOne(XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,Integer orderId,List<SysCreditToRepay> actualRepayDetailList,Boolean compensatory)throws Exception{
        //保存点二，授权预处理冻结借款人存管账户还款总额
        String preTreatRequestNo=XinWangUtil.createRequestNo();
        sendPretreatmentRequest(projectInfo,projectRepayInfo,orderId,preTreatRequestNo,compensatory);
        //保存点二之后
        afterSavePointTwo(projectInfo,projectRepayInfo,orderId,actualRepayDetailList,compensatory,preTreatRequestNo);
    }

    public void afterSavePointTwo(XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,Integer orderId,List<SysCreditToRepay> actualRepayDetailList,Boolean compensatory,String preTreatRequestNo)throws Exception{
        //组装还款批量请求
        List<Map<String,Object>> repayBatchList=repayTransactionService.buildRepayBatchs(projectInfo,actualRepayDetailList,preTreatRequestNo,orderId,compensatory);
        //发送还款批量请求
        int acceptedRepayBatchNum = 0;
        for(Map<String,Object> batch:repayBatchList){
            String batchNo=(String)batch.get("batchNo");
            List<String> requestNos = (List<String>)batch.get("requestNos");
            //发送请求
            String resultJson= null;
            try {
                resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.ASYNC_TRANSACTION.getCode(),batch);
            } catch (Exception e) {
                //异常还款
                repayTransactionService.batchRequestAcceptFail(batchNo,"还款请求受理失败，请重新提交失败的批次",requestNos);
                repayTransactionService.addUnusualRepay(orderId,Integer.valueOf(projectInfo.getProjectNo()));
                LOG.error(e.toString());
                throw new XWTradeException(XWResponseCode.XW_REPAY_ACCEPT_FAIL.getCode(),e.toString());
            }
            Map<String, Object> respMap = JSON.parseObject(resultJson);
            //受理结果
            String code = (String) respMap.get("code");
            String status=(String) respMap.get("status");
            String errorMessage = (String) respMap.get("errorMessage");
            if (("0").equals(code)&& "SUCCESS".equals(status)) {
                repayTransactionService.batchRepayRequestAcceptSuccess(batchNo,batch);
                LOG.info("标"+projectInfo.getProjectNo()+"还款批次"+batchNo+"成功受理");
                acceptedRepayBatchNum++;
            }
            else{
                repayTransactionService.batchRequestAcceptFail(batchNo,resultJson,requestNos);
                repayTransactionService.addUnusualRepay(orderId,Integer.valueOf(projectInfo.getProjectNo()));
                LOG.info("标"+projectInfo.getProjectNo()+"还款批次"+batchNo+"受理失败："+errorMessage);
            }
        }
        //保存点三，还款请求发送完就更新还款进度
        if(acceptedRepayBatchNum==repayBatchList.size()){
            //更新还款进度
            projectRepayInfo.setRepayProgress(XWRepayProgress.XINWANG_REPAY_REQUEST_ACCEPTED);
            Map<String,Object> params=new HashMap<>();
            params.put("id",projectRepayInfo.getId());
            params.put("repayProgress",XWRepayProgress.XINWANG_REPAY_REQUEST_ACCEPTED);
            repayDao.updateProjectRepayInfo(params);
        }
        else{
            String errorLog=String.format(XWResponseCode.XW_REPAY_ACCEPT_FAIL.getMessage(),projectInfo.getProjectNo());
            LOG.error(errorLog);
            throw new XWTradeException(XWResponseCode.XW_REPAY_ACCEPT_FAIL);
        }
        //保存点三之后
        afterSavePointThree(projectInfo,projectRepayInfo,orderId,actualRepayDetailList);
    }

    public void afterSavePointThree(XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,Integer orderId,List<SysCreditToRepay> actualRepayDetailList)throws Exception{
        //组装营销批量请求
        List<Map<String,Object>> marketingBatchList=repayTransactionService.buildMarketingBatchs(projectInfo,actualRepayDetailList,orderId);
        //发送营销批量请求
        int acceptedMarketingBatchNum = 0;
        for(Map<String,Object> batch:marketingBatchList){
            String batchNo=(String)batch.get("batchNo");
            List<String> requestNos = (List<String>)batch.get("requestNos");
            //发送请求
            String resultJson= null;
            try {
                resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.ASYNC_TRANSACTION.getCode(),batch);
            } catch (Exception e) {
                e.printStackTrace();
                repayTransactionService.batchRequestAcceptFail(batchNo,"还款营销请求受理失败，请重新提交失败的批次",requestNos);
                repayTransactionService.addUnusualRepay(orderId,Integer.valueOf(projectInfo.getProjectNo()));
                String errorLog=String.format(XWResponseCode.XW_REPAY_MARKETING_ACCEPT_FAIL.getMessage(),projectInfo.getProjectNo());
                LOG.error(errorLog);
                throw new XWTradeException(XWResponseCode.XW_REPAY_MARKETING_ACCEPT_FAIL);
            }
            Map<String, Object> respMap = JSON.parseObject(resultJson);
            //受理结果
            String code = (String) respMap.get("code");
            String status=(String) respMap.get("status");
            String errorMessage = (String) respMap.get("errorMessage");
            if (("0").equals(code)&& "SUCCESS".equals(status)) {
                repayTransactionService.batchMarketingRequestAcceptSuccess(batchNo, batch);
                LOG.info("标"+projectInfo.getProjectNo()+"营销批次"+batchNo+"成功受理");
                acceptedMarketingBatchNum++;
            }
            else{
                repayTransactionService.batchRequestAcceptFail(batchNo, resultJson,requestNos);
                repayTransactionService.addUnusualRepay(orderId,Integer.valueOf(projectInfo.getProjectNo()));
                LOG.info("标"+projectInfo.getProjectNo()+"营销批次"+batchNo+"受理失败："+errorMessage);
            }
        }
        //保存点四，营销请求发送完就更新还款进度
        if(acceptedMarketingBatchNum==marketingBatchList.size()){
            //更新还款进度
            projectRepayInfo.setRepayProgress(XWRepayProgress.XINWANG_MARKETING_REQUEST_ACCEPTED);
            Map<String,Object> params=new HashMap<>();
            params.put("id",projectRepayInfo.getId());
            params.put("repayProgress",XWRepayProgress.XINWANG_MARKETING_REQUEST_ACCEPTED);
            repayDao.updateProjectRepayInfo(params);
        }
        else{
            String errorLog=String.format(XWResponseCode.XW_REPAY_MARKETING_ACCEPT_FAIL.getMessage(),projectInfo.getProjectNo());
            LOG.error(errorLog);
            throw new XWTradeException(XWResponseCode.XW_REPAY_MARKETING_ACCEPT_FAIL);
        }
        orderService.submit(orderId);
    }

    private void sendPretreatmentRequest(XWProjectInfo projectInfo,SysProjectRepayInfo projectRepayInfo,Integer orderId,String preTreatRequestNo,Boolean compensatory)throws Exception{
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("requestNo", preTreatRequestNo);
        if(compensatory){
            reqData.put("platformUserNo", projectInfo.getGuaranteePlatformUserNo());
            reqData.put("bizType", XWBizType.COMPENSATORY.getCode());
        }
        else{
            reqData.put("platformUserNo", projectInfo.getBorrowerPlatformUserNo());
            reqData.put("bizType", XWBizType.REPAYMENT.getCode());
        }
        reqData.put("amount", projectRepayInfo.getBorrowerRepayAmount());
        reqData.put("projectNo", projectInfo.getProjectNo());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //create order
        XWRequest preTreatReq=new XWRequest();
        preTreatReq.setInterfaceName(XinwangInterfaceName.USER_AUTO_PRE_TRANSACTION.getCode());
        preTreatReq.setRequestNo(preTreatRequestNo);
        preTreatReq.setRequestTime(requestTime);
        preTreatReq.setState(XWRequestState.DQR);
        if(compensatory){
            preTreatReq.setPlatformUserNo(projectInfo.getGuaranteePlatformUserNo());
            XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(Integer.valueOf(projectInfo.getProjectNo()));
            preTreatReq.setUserId(projectExtraInfo.getGuaranteeUserId());
        }
        else{
            preTreatReq.setPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
            preTreatReq.setUserId(projectInfo.getBorrowerUserId());
        }
        preTreatReq.setOrderId(orderId);
        requestDao.createRequest(preTreatReq);
        //保存预处理请求参数
        XWResponseMessage preTreatRequestParams=new XWResponseMessage();
        preTreatRequestParams.setRequestNo(preTreatRequestNo);
        preTreatRequestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(preTreatRequestParams);
        //发送预处理请求
        String resultJson = null;
        try {
            resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.USER_AUTO_PRE_TRANSACTION.getCode(), reqData);
        } catch (Exception e) {
            repayTransactionService.addUnusualRepay(orderId,Integer.valueOf(projectInfo.getProjectNo()));
            LOG.error(e.toString());
            throw new XWTradeException(XWResponseCode.XW_REPAY_PRETREATMENT_FAIL.getCode(),e.toString());
        }
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(preTreatRequestNo);
        responseParams.setResponseMsg(resultJson);
        requestDao.saveResponseMessage(responseParams);
        //处理结果
        String code = (String) respMap.get("code");
        String status=(String) respMap.get("status");
        String errorMessage = (String) respMap.get("errorMessage");
        if ("0".equals(code)&& "SUCCESS".equals(status)) {
            repayTransactionService.xinwangPretreatmentSuccess(preTreatRequestNo,projectRepayInfo);
        }
        else{
            //请求失败或处理失败
            XWRequest param=new XWRequest();
            param.setRequestNo(preTreatRequestNo);
            param.setState(XWRequestState.SB);
            requestDao.updateRequest(param);
            repayTransactionService.addUnusualRepay(orderId,Integer.valueOf(projectInfo.getProjectNo()));
            String errorLog=String.format(XWResponseCode.XW_REPAY_PRETREATMENT_FAIL.getMessage(),projectInfo.getProjectNo(),errorMessage);
            LOG.error(errorLog);
            throw new XWTradeException(XWResponseCode.XW_REPAY_PRETREATMENT_FAIL.getCode(),errorLog);
        }
    }

    @Override
    public void handleNotify(List<Map<String, Object>> details) throws Exception {
        Map<Integer,SysProjectRepayInfo> projectRepayInfoMap=new HashMap<>();
        Map<Integer,List<String>> repaySuccessRequestMap=new HashMap<>();
        Map<Integer,List<String>> marketingSuccessRequestMap=new HashMap<>();
        for(Map<String,Object> response:details){
        try{
            String requestNo=(String)response.get("asyncRequestNo");
            String status=(String)response.get("status");
            String bizTypeStr=(String)response.get("bizType");
            XWTradeType tradeType=XWTradeType.parse(bizTypeStr);
            SysProjectRepayInfo projectRepayInfo=repayDao.getProjectRepayInfoByRequestNo(requestNo);
            Integer projectId=projectRepayInfo.getProjectId();
            projectRepayInfoMap.put(projectId,projectRepayInfo);
            if("SUCCESS".equals(status)){
                repayTransactionService.xinwangRepayFinish(requestNo,tradeType);
                //加入成功的还款请求列表
                if(XWTradeType.REPAYMENT==tradeType||XWTradeType.COMPENSATORY==tradeType){
                    List<String> repaySuccessRequestList=repaySuccessRequestMap.get(projectId);
                    if(repaySuccessRequestList==null){
                        repaySuccessRequestList=new ArrayList<>();
                        repaySuccessRequestMap.put(projectId,repaySuccessRequestList);
                    }
                    repaySuccessRequestList.add(requestNo);
                }
                else if(XWTradeType.MARKETING==tradeType){
                    List<String> marketingSuccessRequestList=marketingSuccessRequestMap.get(projectId);
                    if(marketingSuccessRequestList==null){
                        marketingSuccessRequestList=new ArrayList<>();
                        marketingSuccessRequestMap.put(projectId,marketingSuccessRequestList);
                    }
                    marketingSuccessRequestList.add(requestNo);
                }
            }
            else{
                //结束新网请求
                XWRequest requestParams=new XWRequest();
                requestParams.setRequestNo(requestNo);
                requestParams.setState(XWRequestState.SB);
                requestDao.updateRequest(requestParams);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.info("请求[{}]回调更新时出错",response.get("asyncRequestNo"));
            LOG.info(response);
        }
        }

        for(Integer projectId:projectRepayInfoMap.keySet()){
        try {
            XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
            SysProjectRepayInfo projectRepayInfo=projectRepayInfoMap.get(projectId);
            List<String> repaySuccessRequestList=repaySuccessRequestMap.get(projectId);
            List<String> marketingSuccessRequestList=marketingSuccessRequestMap.get(projectId);
            //平台还款
            if(repaySuccessRequestList!=null){
                repayTransactionService.platfromRepay(projectId,projectInfo,projectRepayInfo,repaySuccessRequestList);
            }
            //平台还加息
            if(marketingSuccessRequestList!=null){
                repayTransactionService.platfromMarketing(projectId,projectInfo,projectRepayInfo,marketingSuccessRequestList);
            }
            //标是否还完
            checkIfRepayFinish(projectInfo);
            //发信息
            try {
                repayTransactionService.sendLetterAndMsg(projectId, projectInfo, projectRepayInfo);
            }
            catch(Exception e){
                LOG.error("标"+projectInfo.getProjectName()+"还款后发送信息出错："+e.getMessage(),e);
            }
        }catch (Exception e){
            LOG.info("标[{}]平台还款或加息时出错",projectId);
            e.printStackTrace();

        }
        }

    }

    private void handleErrorDuringNotify(SysProjectRepayInfo projectRepayInfo,XWProjectInfo projectInfo)throws Exception{
        //新网还款成功，平台还款失败后，用来重新运行平台还款
        List<String> abnormalCreditRepayList=repayDao.getAbnormalCreditRepays(projectRepayInfo.getOrderId());
        List<String> abnormalCreditMarketingList=repayDao.getAbnormalCreditMarketings(projectRepayInfo.getOrderId());
        if(!abnormalCreditRepayList.isEmpty()){
            try{
                repayTransactionService.platfromRepay(projectRepayInfo.getProjectId(),projectInfo,projectRepayInfo,abnormalCreditRepayList);
            }
            catch(Exception e){
                LOG.error("处理还款回调处理异常时出错", e);
            }
        }
        if(!abnormalCreditMarketingList.isEmpty()){
            try{
                repayTransactionService.platfromMarketing(projectRepayInfo.getProjectId(),projectInfo,projectRepayInfo,abnormalCreditMarketingList);
            }
            catch(Exception e){
                LOG.error("处理还款加息回调处理异常时出错", e);
            }
        }
        //标是否还完
        checkIfRepayFinish(projectInfo);
        //发信息
        try {
            repayTransactionService.sendLetterAndMsg(Integer.valueOf(projectInfo.getProjectNo()), projectInfo, projectRepayInfo);
        }
        catch(Exception e){
            LOG.error("标"+projectInfo.getProjectName()+"还款后发送信息出错："+e.getMessage(),e);
        }
    }

    @Override
    public void checkIfRepayFinish(XWProjectInfo projectInfo)throws Exception{
        Integer projectId=Integer.valueOf(projectInfo.getProjectNo());
        Map<String,Object> getProjectRepaymentPlanParams=new HashMap<>();
        getProjectRepaymentPlanParams.put("projectId",projectId);
        List<XWRepaymentPlan> projectRepaymentPlanListFromSlaveDB= repayDao.getRepaymentPlan(getProjectRepaymentPlanParams);
        boolean hasNotYetRepay=false;
        for(XWRepaymentPlan repaymentPlan:projectRepaymentPlanListFromSlaveDB){
            Map<String,Object> params=new HashMap<>();
            params.put("id",repaymentPlan.getId());
            XWRepaymentPlan repaymentPlanFromMarsterDB=repayDao.getRepaymentPlanByUniqueKey(params);
            if(RepaymentPlan_RepayState.WH==repaymentPlanFromMarsterDB.getRepayState()){
                hasNotYetRepay=true;
                break;
            }
        }

        if(!hasNotYetRepay){
            //改变新网标状态
            changeProjectStatusService.changeProjectStatus(Integer.valueOf(projectInfo.getProjectNo()),null, XWProjectStatus.FINISH);
            //改变平台标状态
            repayTransactionService.projectRepayFinish(projectId);
        }
    }

    @Override
    public void saveXWProjectPrepaymentConfig(XWProjectPrepaymentConfig xwProjectPrepaymentConfig) {
        projectDao.saveXWProjectPrepaymentConfig(xwProjectPrepaymentConfig);
    }
}
