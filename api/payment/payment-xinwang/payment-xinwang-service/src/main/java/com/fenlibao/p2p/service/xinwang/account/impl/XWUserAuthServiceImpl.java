package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.AuthList;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.XWUserAuthService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zeronx on 2017/12/21 17:05.
 * @version 1.0
 */
@Service
public class XWUserAuthServiceImpl implements XWUserAuthService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;
    @Resource
    XWAccountDao accountDao;

    @Override
    public boolean cancelUserAuth(Integer userId, String role, String authList) throws Exception {
        Map<String, Object> reqData = new HashMap<String, Object>();
        if (StringUtils.isEmpty(authList)) {
            if (UserRole.INVESTOR.getCode().equals(role)) {
                authList = AuthList.TENDER.getCode();
            } else {
                authList = AuthList.REPAYMENT.getCode();
            }
        }
        Date requestTime = new Date();
        Map<String, Object> params = new HashMap<>();
        params.put("platformUserNo", role.concat("" + userId));
        params.put("authList", "0");

        reqData.put("platformUserNo", role.concat("" + userId));
        reqData.put("requestNo", XinWangUtil.createRequestNo());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("authList", authList);
        LOG.info("开始请求新网取消用户授权接口......");
        String result = XinWangUtil.serviceRequest(XinwangInterfaceName.CANCEL_USER_AUTHORIZATION.getCode(), reqData);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if ("0".equals(jsonObject.getString("code")) && "SUCCESS".equals(jsonObject.getString("status"))) {
            accountDao.updateXWAccount(params);
            LOG.info("取消用户授权保存成功......");
            return true;
        } else {
            LOG.info("取消用户授权异常......");
            throw new XWTradeException(jsonObject.getString("errorMessage"));
        }
    }

    @Transactional
    @Override
    public Map<String, Object> doUserAuth(Integer userId, String role, String authList, Date failTime, BigDecimal amount, String redirectUrl) throws Exception {
        String requestNo = XinWangUtil.createRequestNo();
        Date requestTime = new Date();
        String platformUserNo = role + userId;
        //package the request params
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        if (StringUtils.isEmpty(authList)) {
            if (UserRole.INVESTOR.getCode().equals(role)) {
                authList = AuthList.TENDER.getCode();
            } else {
                authList = AuthList.REPAYMENT.getCode();
            }
        }
        reqData.put("authList", authList);
        if (failTime == null) {
            reqData.put("failTime", new DateTime().plusYears(XinwangConsts.DEFAULT_USER_AUTH_YEAR).toString("yyyyMMdd"));
        } else {
            reqData.put("failTime", new DateTime(failTime).plusYears(XinwangConsts.DEFAULT_USER_AUTH_YEAR).toString("yyyyMMdd"));
        }
        if (amount == null) {
            reqData.put("amount", XinwangConsts.DEFAULT_USER_AUTH_MONEY);
        } else {
            reqData.put("amount", amount);
        }
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP() + redirectUrl);
        LOG.info("开始请求新网用户授权.....");
        Map<String,Object> sendData;
        try {
            sendData=XinWangUtil.gatewayRequest(XinwangInterfaceName.USER_AUTHORIZATION.getCode(),reqData);
        } catch (Exception e) {
            LOG.error("组装新网请求参数失败:", e);
            throw e;
        }
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.USER_AUTHORIZATION.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(userId);
        requestDao.createRequest(req);
        //save request params
        XWResponseMessage message=new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);
        LOG.info("组装新网请求用户授权成功......");
        return sendData;
    }

    @Transactional
    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String platformUserNo = (String)respMap.get("platformUserNo");
        String requestNo = (String)respMap.get("requestNo");
        Map<String, Object> params = new HashMap<>();
        params.put("platformUserNo", platformUserNo);
        params.put("authList", "1");
        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        accountDao.updateXWAccount(params);
        requestDao.updateRequest(requestParams);
    }
}
