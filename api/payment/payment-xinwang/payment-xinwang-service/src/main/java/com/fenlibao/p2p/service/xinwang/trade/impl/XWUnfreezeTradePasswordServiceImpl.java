package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.trade.XWUnfreezeTradePasswordService;
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
 * Created by Administrator on 2017/5/18.
 */
@Service
public class XWUnfreezeTradePasswordServiceImpl implements XWUnfreezeTradePasswordService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    PTCommonDao ptCommonDao;

    @Resource
    PTCommonService ptCommonService;

    @Override
    public void unfreezeTradePassword(Integer userId, String userRole) throws Exception {

        //组装请求
        String requestNo=XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        String platformUserNo = userRole + userId;
        reqData.put("requestNo", requestNo);
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.UNFREEZE_TRADE_PASSWORD.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(platformUserNo);
        requestDao.createRequest(req);
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        //发送请求
        String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.UNFREEZE_TRADE_PASSWORD.getCode(),reqData);
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
            doSuccess(requestNo);
        }
        else{
            doFail(requestNo,errorMessage);
        }

    }

    private void doSuccess(String requestNo) throws Exception{
        //complete request
        XWRequest requestParams=new XWRequest();
        requestParams.setRequestNo(requestNo);
        requestParams.setState(XWRequestState.CG);
        requestDao.updateRequest(requestParams);
    }

    private void doFail(String requestNo, String errorMessage) throws Exception{
        //请求失败或处理失败
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.SB);
        requestDao.updateRequest(param);
        String errorLog=String.format(XWResponseCode.XW_TRADE_UNFREEZE_TRADE_PASSWORD_WRONG.getMessage(),errorMessage);
        LOG.error(errorLog);
        throw new XWTradeException(XWResponseCode.XW_TRADE_UNFREEZE_TRADE_PASSWORD_WRONG.getCode(),errorLog);
    }

}
