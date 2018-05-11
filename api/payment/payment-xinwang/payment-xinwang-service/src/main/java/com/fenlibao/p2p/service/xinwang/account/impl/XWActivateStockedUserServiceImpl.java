package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.consts.XinwangConsts;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.param.account.ActivateStockedUserRequestParams;
import com.fenlibao.p2p.service.xinwang.account.XWActivateStockedUserService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/15.
 */
@Service
public class XWActivateStockedUserServiceImpl implements XWActivateStockedUserService{

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Override
    public Map<String,Object> getActivateStockedUserRequestData(ActivateStockedUserRequestParams params) throws Exception {
        String requestNo= XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        String platformUserNo=params.getPlatformUserNo();
        //package the request params
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+params.getUri());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("failTime", new DateTime().plusYears(XinwangConsts.DEFAULT_USER_AUTH_YEAR).toString("yyyyMMdd"));
        reqData.put("amount", XinwangConsts.DEFAULT_USER_AUTH_MONEY);
        Map<String,Object> sendData=XinWangUtil.gatewayRequest(XinwangInterfaceName.ACTIVATE_STOCKED_USER.getCode(),reqData);
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.ACTIVATE_STOCKED_USER.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(params.getUserId());
        requestDao.createRequest(req);
        //save request params
        XWResponseMessage message=new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);
        return sendData;
    }

    @Transactional
    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String requestNo=(String)respMap.get("requestNo");
        String platformUserNo=(String)respMap.get("platformUserNo");
        String bankcardNo=(String)respMap.get("bankcardNo");
        String auditStatus=(String)respMap.get("auditStatus");
        String bankcode=(String)respMap.get("bankcode");
        String mobile=(String)respMap.get("mobile");
//        String userRole=(String)respMap.get("userRole");
        //complete request
        XWRequest request = requestDao.getByRequestNo(requestNo);
        //if (request != null && request.getState().equals(XWRequestState.DQR)) {
            XWRequest requestParams=new XWRequest();
            requestParams.setRequestNo(requestNo);
            requestParams.setState(XWRequestState.CG);
            requestDao.updateRequest(requestParams);
            //更新新网用户信息
            Map<String,Object> updateXWAccountParams=new HashMap<>();
            updateXWAccountParams.put("platformUserNo",platformUserNo);
            updateXWAccountParams.put("bankcardNo",StringHelper.encode(bankcardNo));
            updateXWAccountParams.put("bankcode",bankcode);
            updateXWAccountParams.put("mobile",mobile);
            updateXWAccountParams.put("auditStatus",auditStatus);
            updateXWAccountParams.put("importUserActivate",1);
            updateXWAccountParams.put("authlist", "1");
            accountDao.updateXWAccount(updateXWAccountParams);
        //}
    }
}
