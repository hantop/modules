package com.fenlibao.p2p.service.xinwang.project.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;
import com.fenlibao.p2p.service.xinwang.project.XWChangeProjectStatusService;
import com.fenlibao.p2p.service.xinwang.query.XWQueryProjectInfomationService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/8.
 */
@Service
public class XWChangeProjectStatusServiceImpl implements XWChangeProjectStatusService{
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;
    @Resource
    XWQueryProjectInfomationService queryProjectInfomationService;
    @Resource
    PTCommonDao commonDao;
    @Resource
    XWProjectDao projectDao;

    @Override
    public void changeProjectStatus(Integer bidId,Integer orderId, XWProjectStatus status){
        XWProjectInfo projectInfo = projectDao.getProjectInfoById(bidId);
        try {
            Map<String, Object> xwProjectInfo = queryProjectInfomationService.queryProjectInformation(Integer.valueOf(projectInfo.getProjectNo()));
            Integer interfaceCode = Integer.valueOf((String) xwProjectInfo.get("code"));
            String interfaceStatus = (String) xwProjectInfo.get("status");
            String projectStatus = (String) xwProjectInfo.get("projectStatus");
            if (GeneralResponseCode.FAIL.getCode().equals(interfaceCode) || GeneralStatus.INIT.getStatus().equals(interfaceStatus)) {
                throw new XWTradeException(XWResponseCode.COMMON_REQUEST_FAIL);
            }
            if (status.getCode().equals(projectStatus)) {
                return;
            }
        } catch (Exception ex) {
            ErrorLogParam errorLogParam = new ErrorLogParam();
            errorLogParam.setMethod("XWChangeProjectStatusService.#changeProjectStatus");
            errorLogParam.setErrorLog(ex.toString());
            commonDao.insertErrorLog(errorLogParam);
            throw new XWTradeException(XWResponseCode.COMMON_REQUEST_FAIL);
        }
        //组装请求
        String requestNo= XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("requestNo", requestNo);
        reqData.put("projectNo", projectInfo.getProjectNo());
        reqData.put("status", status.getCode());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.MODIFY_PROJECT.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
        req.setUserId(projectInfo.getBorrowerUserId());
        req.setOrderId(orderId);
        requestDao.createRequest(req);
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        //发送求
        String resultJson;
        try {
            resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.MODIFY_PROJECT.getCode(),reqData);
        } catch (Exception e) {
            req.setState(XWRequestState.SB);
            requestDao.updateRequest(req);
            LOG.error("标"+projectInfo.getProjectNo()+"改变状态为"+status.getName()+"失败："+"连接超时");
            throw new XWTradeException(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getCode(),String.format(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getMessage(),projectInfo.getProjectNo(),status.getName(),"连接超时"));
        }
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(resultJson);
        requestDao.saveResponseMessage(responseParams);
        //处理结果
        String code = (String) respMap.get("code");
        String errorMessage = (String) respMap.get("errorMessage");
        if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
            req.setState(XWRequestState.CG);
            requestDao.updateRequest(req);
            LOG.info("标"+projectInfo.getProjectNo()+"改变状态为"+status.getName()+"成功");
        }
        else{
            req.setState(XWRequestState.SB);
            requestDao.updateRequest(req);
            LOG.error("标"+projectInfo.getProjectNo()+"改变状态为"+status.getName()+"失败："+errorMessage);
            throw new XWTradeException(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getCode(),String.format(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getMessage(),projectInfo.getProjectNo(),status.getName(),errorMessage));
        }
    }
}
