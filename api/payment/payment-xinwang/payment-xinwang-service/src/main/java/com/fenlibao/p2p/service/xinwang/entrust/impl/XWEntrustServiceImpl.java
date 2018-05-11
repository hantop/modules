package com.fenlibao.p2p.service.xinwang.entrust.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.trust.AuthorizationEntrustRecord;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.CheckType;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.entrust.EntrustTransactionService;
import com.fenlibao.p2p.service.xinwang.entrust.XWEntrustService;
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
 * @date 2017/7/13 14:44
 */
@Service
public class XWEntrustServiceImpl implements XWEntrustService {
    private final Logger LOG = LogManager.getLogger(this.getClass());
    @Resource
    private XWRequestDao requestDao;
    @Resource
    private XWProjectDao projectDao;
    @Resource
    private XWAccountDao accountDao;
    @Resource
    private EntrustTransactionService entrustTransactionService;

    @Override
    public AuthorizeStatus authEntrustPay(Integer projectId) throws Exception {
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectId);
        XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(projectId);
        if(projectExtraInfo.getEntrustPayeeUserId()==null){
            throw new XWTradeException(XWResponseCode.XW_ENTRUST_AUTHORIZATION_FAIL.getCode(),"委托收款人id为空");
        }
        //组装请求
        String borrowPlatformUserNo = projectInfo.getBorrowerPlatformUserNo();
        String entrustedPlatformUserNo=projectInfo.getEntrustPayeePlatformUserNo();
        PlatformAccount platformAccount=accountDao.getPlatformAccountInfoByPlatformUserNo(entrustedPlatformUserNo);
        String requestNo=XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("requestNo", requestNo);
        reqData.put("borrowPlatformUserNo", borrowPlatformUserNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("projectNo", projectId+"");
        reqData.put("entrustedPlatformUserNo" ,entrustedPlatformUserNo);
        reqData.put("checkType", CheckType.LIMIT.getCode());
        reqData.put("entrustedType", platformAccount.getUserType().getCode());
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.AUTHORIZATION_ENTRUST_PAY.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(borrowPlatformUserNo);
        req.setUserId(projectInfo.getBorrowerUserId());
        requestDao.createRequest(req);
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        //发送请求
        String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.AUTHORIZATION_ENTRUST_PAY.getCode(),reqData);
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(resultJson);
        requestDao.saveResponseMessage(responseParams);
        //处理结果
        String code = (String) respMap.get("code");
        String errorMessage = (String) respMap.get("errorMessage");
        AuthorizeStatus status = AuthorizeStatus.parse((String) respMap.get("authorizeStatus"));
        if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
            entrustTransactionService.doSuccess(requestNo,projectId,status);
        }
        else{
            entrustTransactionService.doFail(requestNo,projectId, errorMessage,status);
        }
        return status;
    }

    @Override
    public AuthorizationEntrustRecord getAuthEntrustRecord(String requestNo) {
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("requestNo", requestNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        //发送请求
        String resultJson="";
        try {
            resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.QUERY_AUTHORIZATION_ENTRUST_PAY_RECORD.getCode(), reqData);
        }catch (Exception e){
            LOG.error(e);
        }
        //处理结果
        AuthorizationEntrustRecord record = JSONObject.parseObject(resultJson, AuthorizationEntrustRecord.class);
        if (!record.validate()) {
            throw new XWTradeException(XWResponseCode.XW_ENTRUST_AUTHORIZATION_RECORD_QUERY_FAIL);
        }
        return record;
    }
}
