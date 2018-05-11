package com.fenlibao.p2p.service.xinwang.project.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.query.ProjectQueryInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.XWProjectStatus;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.project.XWProjectService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class XWProjectServiceImpl implements XWProjectService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());
    @Resource
    private XWRequestDao requestDao;

    @Override
    public void changeProjectStatus(XWProjectInfo projectInfo, PTProjectState status) throws Exception {
        //改变平台标状态
        XWProjectStatus xwStatus = XWProjectStatus.COLLECTING;
        switch (status) {
            case TBZ:
                xwStatus = XWProjectStatus.COLLECTING;
                break;
            case HKZ:
                xwStatus = XWProjectStatus.REPAYING;
                break;
            case YJQ:
                xwStatus = XWProjectStatus.FINISH;
                break;
            case YLB:
                xwStatus = XWProjectStatus.MISCARRY;
                break;
            default:
                break;
        }
        //先查询标的状态
        ProjectQueryInfo projectQueryInfo = this.queryProject(Integer.valueOf(projectInfo.getProjectNo()));
        if (StringUtils.isEmpty(projectQueryInfo.getProjectStatus())) {
            throw new XWTradeException(XWResponseCode.COMMON_REQUEST_FAIL);
        }
        if (!projectQueryInfo.getProjectStatus().equals(xwStatus.getCode())) {
            //组装请求
            String requestNo= XinWangUtil.createRequestNo();
            Date requestTime=new Date();
            Map<String,Object> reqData=new HashMap<>();
            reqData.put("requestNo", requestNo);
            reqData.put("projectNo", projectInfo.getProjectNo());
            reqData.put("status", xwStatus.getCode());
            reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
            //create order
            XWRequest req=new XWRequest();
            req.setInterfaceName(XinwangInterfaceName.MODIFY_PROJECT.getCode());
            req.setRequestNo(requestNo);
            req.setRequestTime(requestTime);
            req.setState(XWRequestState.DQR);
            req.setPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
            req.setUserId(projectInfo.getBorrowerUserId());
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
                LOG.error("标"+projectInfo.getProjectNo()+"改变状态为"+xwStatus.getName()+"失败："+"连接超时");
                throw new XWTradeException(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getCode(),String.format(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getMessage(),projectInfo.getProjectNo(),xwStatus.getName(),"连接超时"));
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
                LOG.info("标"+projectInfo.getProjectNo()+"改变状态为"+xwStatus.getName()+"成功");
            } else{
                req.setState(XWRequestState.SB);
                requestDao.updateRequest(req);
                LOG.error("标"+projectInfo.getProjectNo()+"改变状态为"+xwStatus.getName()+"失败："+errorMessage);
               throw new XWTradeException(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getCode(),String.format(XWResponseCode.XW_CHANGE_PROJECT_STATUS_WRONG.getMessage(),projectInfo.getProjectNo(),xwStatus.getName(),errorMessage));
            }
        }
    }

    @Override
    public ProjectQueryInfo queryProject(Integer bidId) throws Exception {
        //组装请求
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("projectNo", bidId);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //发送请求
        String resultJson= XinWangUtil.serviceRequest(XinwangInterfaceName.QUERY_PROJECT_INFORMATION.getCode(),reqData);
        return JSONObject.parseObject(resultJson,ProjectQueryInfo.class);
    }
}
