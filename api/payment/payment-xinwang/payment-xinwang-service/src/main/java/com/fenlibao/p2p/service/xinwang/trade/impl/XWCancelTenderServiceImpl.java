package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysCancelTenderDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectCancelTenderInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.order.XWTradeOrderType;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_Cancel;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_MakeLoan;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.project.XWChangeProjectStatusService;
import com.fenlibao.p2p.service.xinwang.trade.XWCancelTenderService;
import com.fenlibao.p2p.service.xinwang.trade.XWCancelTenderTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流标
 */
@Service
public class XWCancelTenderServiceImpl implements XWCancelTenderService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    SysOrderManageDao orderManageDao;

    @Resource
    SysCancelTenderDao cancelTenderDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysOrderService orderService;

    @Resource
    PTCommonDao commonDao;

    @Resource
    XWChangeProjectStatusService changeProjectStatusService;

    @Resource
    XWCancelTenderTransactionService cancelTenderTransactionService;

    @Override
    public void cancelTenders(Integer projectId) throws Exception{
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
        if(projectInfo==null){
            throw new XWTradeException(XWResponseCode.BID_NOT_EXIST);
        }
        if(projectInfo.getState()!= PTProjectState.DFK&&projectInfo.getState()!= PTProjectState.TBZ){
            throw new XWTradeException(XWResponseCode.TRADE_FLOW_CONDITIONS_NOT_SATISFIED);
        }
        Integer orderId=cancelTenderDao.getOngoingCancelTenderOrder(projectId);
        if(orderId!=null){
            throw new XWTradeException(XWResponseCode.TRADE_OPERATION_REPEAT);
        }
        //出借记录
        Map<String,Object> tenderRecordParams=new HashMap<>(3);
        tenderRecordParams.put("projectNo", projectId);
        tenderRecordParams.put("cancel", TenderRecord_Cancel.F.name());
        tenderRecordParams.put("makeLoan", TenderRecord_MakeLoan.F.name());
        List<XWTenderRecord> tenderRecordList=projectDao.getTenderRecord(tenderRecordParams);
        //创建平台整标流标订单
        SystemOrder systemOrder = new SystemOrder();
        systemOrder.setTypeCode(XWTradeOrderType.PROJECT_CANCEL_TENDER.orderType());
        systemOrder.setOrderStatus(XWOrderStatus.DTJ);
        systemOrder.setSource(Source.HT);
        orderManageDao.add(systemOrder);
        //保存流标訂單信息
        SysProjectCancelTenderInfo projectCancelTenderInfo=new SysProjectCancelTenderInfo();
        projectCancelTenderInfo.setProjectId(projectId);
        projectCancelTenderInfo.setOrderId(systemOrder.getId());
        cancelTenderDao.createProjectCancelTenderInfo(projectCancelTenderInfo);

        xinwangCancelTender(systemOrder.getId(),projectId,projectInfo,tenderRecordList);
    }

    private void xinwangCancelTender(Integer orderId,Integer projectId,XWProjectInfo projectInfo,List<XWTenderRecord> tenderRecordList)throws Exception{
        //当前时间
        Date currentDate=commonDao.getCurrentDate();
        List<XWTenderRecord> cancelSuccessTenderRecordList=new ArrayList<>();
        for(XWTenderRecord tenderRecord:tenderRecordList){
            //组装请求
            String requestNo= XinWangUtil.createRequestNo();
            Map<String,Object> reqData=new HashMap<>();
            reqData.put("requestNo", requestNo);
            reqData.put("preTransactionNo", tenderRecord.getPreTreatRequestNo());
            reqData.put("amount", tenderRecord.getAmount());
            reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(currentDate));
            //创建新网订单
            XWRequest req=new XWRequest();
            req.setInterfaceName(XinwangInterfaceName.CANCEL_PRE_TRANSACTION.getCode());
            req.setRequestNo(requestNo);
            req.setRequestTime(currentDate);
            req.setState(XWRequestState.DQR);
            req.setOrderId(orderId);
            requestDao.createRequest(req);
            //保存请求参数
            XWResponseMessage requestParams=new XWResponseMessage();
            requestParams.setRequestNo(requestNo);
            requestParams.setRequestParams(JSON.toJSONString(reqData));
            requestDao.saveRequestMessage(requestParams);
            //保存流标流水号到新网出借记录表
            Map<String,Object> saveRequestNoParams=new HashMap<>();
            saveRequestNoParams.put("tenderId",tenderRecord.getId());
            saveRequestNoParams.put("cancelTenderRequestNo",requestNo);
            cancelTenderDao.saveCancelTenderRequestNo(saveRequestNoParams);
            //发送求
            String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.CANCEL_PRE_TRANSACTION.getCode(),reqData);
            Map<String, Object> respMap = JSON.parseObject(resultJson);
            //保存返回报文
            XWResponseMessage responseParams=new XWResponseMessage();
            responseParams.setRequestNo(requestNo);
            responseParams.setResponseMsg(resultJson);
            requestDao.saveResponseMessage(responseParams);
            //处理结果
            String code = (String) respMap.get("code");
            String status = (String) respMap.get("status");
            String errorMessage = (String) respMap.get("errorMessage");
            if (("0").equals(code)&& "SUCCESS".equals(status)) {
                req.setState(XWRequestState.CG);
                requestDao.updateRequest(req);
                cancelSuccessTenderRecordList.add(tenderRecord);
                LOG.info("流标订单"+orderId+"，出借"+tenderRecord.getId()+"存管撤销出借成功");
            }
            else{
                req.setState(XWRequestState.SB);
                requestDao.updateRequest(req);
                LOG.info("流标订单"+orderId+"，出借"+tenderRecord.getId()+"存管撤销出借失败:"+errorMessage);
            }
        }

        orderService.submit(orderId);
        afterXinwangCancelTender(orderId,projectId,projectInfo,cancelSuccessTenderRecordList,currentDate);
    }

    private void afterXinwangCancelTender(Integer orderId,Integer projectId,XWProjectInfo projectInfo,List<XWTenderRecord> cancelSuccessTenderRecordList,Date currentDate)throws Exception{
        //平台流标
        cancelTenderTransactionService.platformCancelTender(orderId, projectId,projectInfo,cancelSuccessTenderRecordList);

        if (PTProjectState.DFK == projectInfo.getState()) {
            // 判断是否全部出借均已取消,是则更新标状态为已流标
            Map<String,Object> tenderRecordParams=new HashMap<>(3);
            tenderRecordParams.put("projectNo", projectId);
            tenderRecordParams.put("cancel", TenderRecord_Cancel.F.name());
            tenderRecordParams.put("makeLoan", TenderRecord_MakeLoan.F.name());
            List<XWTenderRecord> notYetCancelList=projectDao.getTenderRecord(tenderRecordParams);
            if (notYetCancelList.isEmpty()) {
                //改变新网标状态
                changeProjectStatusService.changeProjectStatus(Integer.valueOf(projectInfo.getProjectNo()),orderId, XWProjectStatus.MISCARRY);
                //改变平台标状态
                cancelTenderTransactionService.finishCancelTender(projectId,orderId,currentDate);
            }
        }
    }

    @Override
    public void handleError(Integer orderId)throws Exception{
        SysProjectCancelTenderInfo projectCancelTenderInfo= cancelTenderDao.getProjectCancelTenderInfo(orderId);
        Integer projectId=projectCancelTenderInfo.getProjectId();
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
        //处理流标受理失败的出借
        List<XWTenderRecord> acceptFailTenderRecordList=cancelTenderDao.getAcceptFailTenderRecordList(projectId);
        if(!acceptFailTenderRecordList.isEmpty()){
            xinwangCancelTender(orderId,projectId,projectInfo,acceptFailTenderRecordList);
        }
        //处理新网流标成功，平台流标失败的出借
        List<XWTenderRecord> platformCancelTenderFailList=cancelTenderDao.getPlatformCancelTenderFailList(projectId);
        if(!platformCancelTenderFailList.isEmpty()){
            Date currentDate=commonDao.getCurrentDate();
            afterXinwangCancelTender(orderId,projectId,projectInfo,platformCancelTenderFailList,currentDate);
        }
    }


}
