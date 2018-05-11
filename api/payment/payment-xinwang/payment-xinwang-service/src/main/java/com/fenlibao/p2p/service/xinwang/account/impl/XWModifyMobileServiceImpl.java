package com.fenlibao.p2p.service.xinwang.account.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.param.account.ModifyMobileRequestParams;
import com.fenlibao.p2p.service.xinwang.account.XWModifyMobileService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
@Service
public class XWModifyMobileServiceImpl implements XWModifyMobileService{
    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Transactional
    @Override
    public Map<String, Object> getModifyMobileRequestData(ModifyMobileRequestParams params) throws Exception {
        Map<String, Object> reqData = new HashMap<>();

        String requestNo=XinWangUtil.createRequestNo();
        Date createTime = new Date();
        String platformUserNo = params.getUserRole() + params.getUserId();

        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        //reqData.put("checkType", CheckType.LIMIT.getCode());
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+params.getUri());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(createTime));
        Map<String, Object> sendData = XinWangUtil.gatewayRequest(XinwangInterfaceName.MODIFY_MOBILE_EXPAND.getCode(),reqData);

        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.MODIFY_MOBILE_EXPAND.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(createTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
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
        String userRole=(String)respMap.get("userRole");

        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);
    }
}
