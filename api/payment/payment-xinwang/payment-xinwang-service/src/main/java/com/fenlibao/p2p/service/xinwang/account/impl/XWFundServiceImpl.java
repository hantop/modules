package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.XWFundService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class XWFundServiceImpl implements XWFundService{
    protected final Logger LOG = LogManager.getLogger(this.getClass());
    @Resource
    XWRequestDao requestDao;
    @Resource
    PTCommonDao commonDao;
    @Resource
    XWAccountDao accountDao;
    @Resource
    XWUserInfoService xwUserInfoService;

    @Override
    public void doFreezeFund(Integer userId, BigDecimal amount) throws Exception {
        Map<String, Object> reqData = new HashMap<String, Object>();
        String requestNo = XinWangUtil.createRequestNo();
        Date requestTime = new Date();

        String platformUserNo = UserRole.INVESTOR.getCode() + userId;
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("amount", amount);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("generalFreezeRequestNo", requestNo);

        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.FREEZE.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(userId);
        requestDao.createRequest(req);
        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(requestNo);
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);
        String result = XinWangUtil.serviceRequest(XinwangInterfaceName.FREEZE.getCode(), reqData);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);
        BaseResult baseResult = JSONObject.parseObject(result, BaseResult.class);
        if (baseResult == null || !baseResult.validate()) {
            throw new XWTradeException(XWResponseCode.FUND_FREEZE_FAIL);
        }

        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);
    }

    @Override
    public void doUnFreezeFund(Integer userId, BigDecimal amount, String sourceNo) throws Exception {
        Map<String, Object> reqData = new HashMap<String, Object>();
        String requestNo = XinWangUtil.createRequestNo();
        String platformUserNo = UserRole.INVESTOR.getCode() + userId;
        Date requestTime = new Date();

        reqData.put("platformUserNo", platformUserNo);
        reqData.put("amount", amount);
        if (StringUtils.isNotBlank(sourceNo)) {
            reqData.put("originalFreezeRequestNo", sourceNo);
        }
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("requestNo", requestNo);

        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.UNFREEZE.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(userId);
        requestDao.createRequest(req);

        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(requestNo);
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);
        String result = XinWangUtil.serviceRequest(XinwangInterfaceName.UNFREEZE.getCode(), reqData);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);
        BaseResult baseResult = JSONObject.parseObject(result, BaseResult.class);
        if (baseResult == null || !baseResult.validate() ) {
            throw new XWTradeException(XWResponseCode.FUND_FREEZE_FAIL);
        }

        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);
    }
}
