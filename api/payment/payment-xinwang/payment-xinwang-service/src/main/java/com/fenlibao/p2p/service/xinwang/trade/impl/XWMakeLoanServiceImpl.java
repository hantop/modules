package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectRate;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectConfirmTenderInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.order.XWTradeOrderType;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.ProjectType;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_Cancel;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_MakeLoan;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.project.XWProjectService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanService;
import com.fenlibao.p2p.service.xinwang.trade.XWMakeLoanTransationService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by Administrator on 2017/5/23.
 */
@Service
public class XWMakeLoanServiceImpl implements XWMakeLoanService{

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;
    @Resource
    XWAccountDao accountDao;
    @Resource
    PTCommonDao commonDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysMakeLoanDao makeLoanDao;

    @Resource
    SysOrderManageDao orderManageDao;

    @Resource
    SysOrderService orderService;

    @Resource
    XWProjectService projectService;

    @Resource
    XWMakeLoanTransationService makeLoanTransationService;

    @Override
    public void makeLoanApply(Integer loanId) throws Exception {
        //标信息
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(loanId);
        if(projectInfo.getState()!= PTProjectState.DFK){
            throw new XWTradeException(XWResponseCode.TRADE_MAKE_A_LOAN_CONDITIONS_NOT_SATISFIED);
        }
        //是否委托收款
        if(projectInfo.getProjectType()==ProjectType.ENTRUST_PAY){
            checkIfEntrustPayAuthorized(projectInfo.getEntrustPayAuthorizeRequestNo());
        }
        //投资记录
        Map<String,Object> tenderRecordParams=new HashMap<>(3);
        tenderRecordParams.put("projectNo", loanId);
        tenderRecordParams.put("cancel", TenderRecord_Cancel.F.name());
        tenderRecordParams.put("makeLoan", TenderRecord_MakeLoan.F.name());
        List<XWTenderRecord> tenderRecordList=projectDao.getTenderRecord(tenderRecordParams);
        //没有待放款投资记录
        if (tenderRecordList.isEmpty()) {
            return;
        }
        //创建平台整标放款订单
        SystemOrder systemOrder = new SystemOrder();
        systemOrder.setTypeCode(XWTradeOrderType.PROJECT_CONFIRM_TENDER.orderType());
        systemOrder.setOrderStatus(XWOrderStatus.DTJ);
        systemOrder.setSource(Source.HT);
        orderManageDao.add(systemOrder);
        //保存放款訂單信息
        SysProjectConfirmTenderInfo projectConfirmTenderInfo=new SysProjectConfirmTenderInfo();
        projectConfirmTenderInfo.setProjectId(loanId);
        projectConfirmTenderInfo.setOrderId(systemOrder.getId());
        makeLoanDao.createProjectConfirmTenderInfo(projectConfirmTenderInfo);
        //发送放款请求
        boolean isSuccess = sendTenderConfirmRequest(systemOrder.getId(),loanId,tenderRecordList,projectInfo);
        if (isSuccess) {
            orderService.submit(systemOrder.getId());
        }else{
            LOG.info("平台放款订单失败，订单ID[{}]",systemOrder.getId());
        }
    }

    private void checkIfEntrustPayAuthorized(String entrustPayAuthorizeRequestNo)throws Exception{
        //组装请求
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("requestNo", entrustPayAuthorizeRequestNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //发送求
        String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.QUERY_AUTHORIZATION_ENTRUST_PAY_RECORD.getCode(),reqData);
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //处理结果
        String code = (String) respMap.get("code");
        String status = (String) respMap.get("status");
        String errorMessage = (String) respMap.get("errorMessage");
        if (("0").equals(code)&& "SUCCESS".equals(status)) {
            AuthorizeStatus authorizeStatus = AuthorizeStatus.parse((String) respMap.get("authorizeStatus"));
            if(authorizeStatus==AuthorizeStatus.AUDIT||authorizeStatus==AuthorizeStatus.FAIL){
                throw new XWTradeException(XWResponseCode.XW_CONFIRM_TENDER_FAIL.getCode()
                        ,String.format(XWResponseCode.XW_CONFIRM_TENDER_FAIL.getMessage(),"委托开户授权结果",authorizeStatus.getName()));
            }
        }
        else{
            throw new XWTradeException(XWResponseCode.XW_CONFIRM_TENDER_FAIL.getCode()
                    ,String.format(XWResponseCode.XW_CONFIRM_TENDER_FAIL.getMessage(),"查询委托开户授权结果失败",errorMessage));
        }
    }

    private boolean sendTenderConfirmRequest(Integer orderId,Integer projectId,List<XWTenderRecord> tenderRecordList,XWProjectInfo projectInfo)throws Exception{
        //查出费率
        XWProjectRate projectRate=projectDao.getProjectRateById(projectId);
        //如果待放款的投资记录数大于batch最大请求数，分到多个batch
        BigDecimal tenderRecordListSize=new BigDecimal(tenderRecordList.size());
        BigDecimal batchSize=new BigDecimal(XinwangConsts.MAX_REQUESTS_PER_BATCH);
        int batchNum = tenderRecordListSize.divide(batchSize, 0, RoundingMode.UP).intValue();
        //组装batch
        List<Map<String, Object>> batchList = makeLoanTransationService.buildBatchs(projectId, tenderRecordList, projectRate, orderId, batchNum,projectInfo);

        for(Map<String,Object> batch:batchList){
            String batchNo=(String)batch.get("batchNo");
            //发送请求
            String resultJson = "";
            try {
				resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.ASYNC_TRANSACTION.getCode(),batch);
			} catch (Exception e) {
			    //通过订单控制流程，必须处理异常
                XWRequest request = new XWRequest();
                request.setBatchNo(batchNo);
                makeLoanTransationService.confirmTenderAcceptFail(request, orderId,"批量放款失败");
                e.printStackTrace();
				LOG.info("标"+projectId+"放款批次"+batchNo+"受理失败");
                return false;
            }
            Map<String, Object> respMap = JSON.parseObject(resultJson);
            //批次受理结果
            String code = (String) respMap.get("code");
            String errorMessage = (String) respMap.get("errorMessage");
            List<String> requestNos = (List<String>)batch.get("requestNos");
            if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
                XWRequest updatequestParam=new XWRequest();
                updatequestParam.setRequestNos(requestNos);
                updatequestParam.setBatchNo(batchNo);
                updatequestParam.setState(XWRequestState.DQR);
                requestDao.updateRequest(updatequestParam);
                LOG.info("标"+projectId+"放款批次"+batchNo+"成功受理");
            }else{
                XWRequest request = new XWRequest();
                request.setRequestNos(requestNos);
                request.setBatchNo(batchNo);
                makeLoanTransationService.confirmTenderAcceptFail(request,orderId,resultJson);
                LOG.info("标"+projectId+"放款批次"+batchNo+"受理失败："+errorMessage);
                return false;
            }
        }
        return true;
    }

    /**
     * 先查明问题原因，不满足放款条件的先满足，再重新发请求
     * @param orderId
     * @throws Exception
     */
    @Override
    public void handleError(Integer orderId)throws Exception{
        SysProjectConfirmTenderInfo projectConfirmTenderInfo= makeLoanDao.getProjectConfirmTenderInfoByOrderId(orderId);
        Integer projectId=projectConfirmTenderInfo.getProjectId();
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
        //批次受理失败或者单个请求失败
        List<XWTenderRecord> acceptFailTenderRecordList=makeLoanDao.getAcceptFailTenderRecordList(projectId);
        if(!acceptFailTenderRecordList.isEmpty()){
            sendTenderConfirmRequest(orderId,projectId,acceptFailTenderRecordList,projectInfo);
        }
        //新网放款成功，平台处理失败的
        List<String> confirmTenderSuccessRequestList=makeLoanDao.getPlatformConfirmTenderFailRequestList(projectId);
        int i = 0;
        if(!confirmTenderSuccessRequestList.isEmpty()){
            //平台放款
            //makeLoanTransationService.platformMakeLoan(projectInfo,confirmTenderSuccessRequestList,orderId);
            i++;
        }
        //检查标是否放完款
        checkIfFinish(projectId);
    }

    @Override
    public void handleNotify(List<Map<String, Object>> details) throws Exception {
        for(Map<String,Object> response:details){
            String requestNo=(String)response.get("asyncRequestNo");
            String status=(String)response.get("status");
            //平台放款
            XWRequest request = requestDao.getByRequestNo(requestNo);
            if("SUCCESS".equals(status)){
                makeLoanTransationService.platformMakeLoan(request.getRequestNo(),request.getOrderId());
            }
            else{
                XWRequest requestParam = new XWRequest();
                requestParam.setRequestNo(request.getRequestNo());
                makeLoanTransationService.confirmTenderAcceptFail(request,request.getOrderId(),"批量放款失败");
            }
        }
    }

    @Override
    public void checkIfFinish(Integer bidId)throws Exception{
        // 判断是否全部投资均已成功放款
        Map<String,Object> tenderRecordParams=new HashMap<>(1);
        tenderRecordParams.put("projectNo", bidId);
        List<XWTenderRecord> tenderRecordList = projectDao.getTenderRecord(tenderRecordParams);
        // A:必须有投资记录; B:所有投资记录不能为未放款状态
        if (tenderRecordList.isEmpty()) {
            return;
        }
        boolean hasNotYetMakeLoanTender=false;
        for(XWTenderRecord slaveItem:tenderRecordList){
            if(TenderRecord_MakeLoan.F==slaveItem.getMakeLoan()){
                hasNotYetMakeLoanTender=true;
                break;
            }
        }

        if(!hasNotYetMakeLoanTender){
            XWProjectInfo projectInfo = projectDao.getProjectInfoById(bidId);
            //改变新网标状态
            projectService.changeProjectStatus(projectInfo, PTProjectState.HKZ);
            XWProjectExtraInfo extraInfo = projectDao.getProjectExtraInfo (bidId);

            XWProjectRate projectRate=projectDao.getProjectRateById(bidId);

            makeLoanTransationService.sendLetterAfterMakeLoan(projectInfo);

            makeLoanTransationService.generateRepaymentPlan(projectInfo,extraInfo.getBearInterestDate(),extraInfo.getEndDate(),extraInfo,projectRate);
        }
    }



    @Override
    public Integer getOngoingConfirmTenderOrder(Integer projectId) {
        return makeLoanDao.getOngoingConfirmTenderOrder(projectId);
    }
}
