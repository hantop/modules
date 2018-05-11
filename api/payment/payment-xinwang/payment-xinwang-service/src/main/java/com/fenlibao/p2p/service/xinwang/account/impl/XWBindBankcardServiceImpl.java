package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.CheckType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.XWBindBankcardService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class XWBindBankcardServiceImpl implements XWBindBankcardService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Override
    public Map<String, Object> getBindcardParam(Integer userId, String uri, UserRole userRole) throws Exception {
        String requestNo = XinWangUtil.createRequestNo();
        Date requestTime = new Date();
        String platformUserNo = userRole.getCode() + userId;
        //package the request params
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("checkType", CheckType.LIMIT.getCode());
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP() + uri);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        Map<String, Object> sendData = XinWangUtil.gatewayRequest(XinwangInterfaceName.PERSONAL_BIND_BANKCARD_EXPAND.getCode(), reqData);
        //create order
        XWRequest req = new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.PERSONAL_BIND_BANKCARD_EXPAND.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(userId);
        requestDao.createRequest(req);
        //save request params
        XWResponseMessage message = new XWResponseMessage();
        message.setRequestNo(requestNo);
        message.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(message);
        return sendData;
    }

    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String requestNo = (String) respMap.get("requestNo");
        String code = (String) respMap.get("code");
        String status = (String) respMap.get("status");
        String platformUserNo = (String) respMap.get("platformUserNo");
        String bankcardNo = (String) respMap.get("bankcardNo");
        String bankcode = (String) respMap.get("bankcode");

        //更新绑卡银行和卡号
        Map<String, Object> params = new HashMap<>();
        params.put("platformUserNo", platformUserNo);
        String encodeBankcardNo = StringHelper.encode(bankcardNo);
        params.put("bankcardNo", encodeBankcardNo);
        params.put("bankcode", bankcode);
        accountDao.updateXWAccount(params);

        XWRequest requestParams = new XWRequest();
        if (GeneralResponseCode.FAIL.equals(code) || GeneralStatus.INIT.equals(status)) {//充值失败
            //complete request
            requestParams.setRequestNo(requestNo);
            requestParams.setState(XWRequestState.SB);
            requestDao.updateRequest(requestParams);
            throw new XWTradeException(XWResponseCode.PAYMENT_BIND_CARD_FAIL);
        }
        //complete request
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);
    }

    @Override
    public List<String> getOrderNeedComfired(List<XinwangInterfaceName> interfaceNames, Date requestTime) {
        List<String> requestNoList = new ArrayList<>();
        for (XinwangInterfaceName interfaceName : interfaceNames) {
            List<String> req = requestDao.getOrderNeedComfired(interfaceName, requestTime);
            requestNoList.addAll(req);
        }
        return requestNoList;
    }

    @Transactional
    @Override
    public void comfiredOrder(String platformUserNo, String bankcardNo, String bankcode, String requestNo) {
        try {
            //更新绑卡银行和卡号
            Map<String, Object> params = new HashMap<>();
            params.put("platformUserNo", platformUserNo);
            String encodeBankcardNo = null;
            encodeBankcardNo = StringHelper.encode(bankcardNo);
            params.put("bankcardNo", encodeBankcardNo);
            params.put("bankcode", bankcode);
            params.put("importUserActivate", 1);
            accountDao.updateXWAccount(params);
        } catch (Exception e) {
            throw new XWTradeException(XWResponseCode.COMMON_PARAM_ENCRYPT_FAILURE);
        }
        XWRequest request = new XWRequest();
        request.setRequestNo(requestNo);
        request.setState(XWRequestState.CG);
        requestDao.updateRequest(request);
    }
}