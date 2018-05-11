package com.fenlibao.p2p.service.xinwang.trade.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.trade.PreTransactionResult;
import com.fenlibao.p2p.model.xinwang.entity.trade.SyncTransactionResult;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.transaction.PreTransactionParam;
import com.fenlibao.p2p.model.xinwang.param.transaction.SyncTransactionParam;
import com.fenlibao.p2p.service.xinwang.trade.XWTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @date 2017/5/11 16:04
 */
@Service
public class XWTransactionServiceImpl implements XWTransactionService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(XWTransactionService.class);

    @Resource
    private XWRequestDao requestDao;

    @Override
    public PreTransactionResult preTransaction(PreTransactionParam param) throws Exception {
        Map<String, Object> reqData = new HashMap<>(6);
        reqData.put("platformUserNo", param.getPlatformUserNo());
        reqData.put("requestNo", param.getRequestNo());
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        reqData.put("amount", param.getAmount());
        reqData.put("bizType", param.getBizType().getCode());
        reqData.put("projectNo", param.getProjectNo());
        reqData.put("share", param.getShare());
        reqData.put("creditsaleRequestNo", param.getCreditsaleRequestNo());
        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(param.getRequestNo());
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);
        String result = "";
        result = XinWangUtil.serviceRequest(XinwangInterfaceName.USER_AUTO_PRE_TRANSACTION.getCode(), reqData);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(param.getRequestNo());
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);
        PreTransactionResult transactionResult = JSONObject.parseObject(result, PreTransactionResult.class);
        return transactionResult;
    }

    @Override
    public SyncTransactionResult doSyncTransaction(SyncTransactionParam param) throws Exception {
        String requestNo = XinWangUtil.createRequestNo();
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("tradeType", param.getTradeType().getCode());
        if (StringUtils.isNotBlank(param.getProjectNo())) {
            reqData.put("projectNo", param.getProjectNo());
        }
        reqData.put("requestNo", requestNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        List<Object> details=new ArrayList<Object>();
        Map<String, Object> detailsItem = new HashMap<String, Object>();
        detailsItem.put("bizType", param.getBizType().getCode());
        detailsItem.put("amount", param.getAmount());
        if (StringUtils.isNotBlank(param.getFreezeRequestNo())) {
            detailsItem.put("freezeRequestNo", param.getFreezeRequestNo());
        }
        detailsItem.put("sourcePlatformUserNo", param.getSourcePlatformUserNo());
        detailsItem.put("targetPlatformUserNo", param.getTargetPlatformUserNo());//标借款人
        if (param.getShare()!=null) {
            detailsItem.put("share", param.getShare());
        }
        if (StringUtils.isNotBlank(param.getRemark())) {
            detailsItem.put("remark", param.getRemark());
        }
        details.add(detailsItem);
        reqData.put("details", details);
        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(requestNo);
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);
        String result = "";
        result = XinWangUtil.serviceRequest(XinwangInterfaceName.SYNC_TRANSACTION.getCode(),reqData);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);
        SyncTransactionResult transactionInfo = JSONObject.parseObject(result, SyncTransactionResult.class);
        if (transactionInfo == null) {
            logger.warn("单笔交易失败，requestNo=[{}],platformUserNo=[{}]", requestNo, param.getSourcePlatformUserNo());
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG);
        }
        return transactionInfo;
    }

    @Override
    public BaseResult cancelTransaction(String preTransactionNo, BigDecimal amount) {
        String requestNo = XinWangUtil.createRequestNo();
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("requestNo", requestNo);
        reqData.put("preTransactionNo", preTransactionNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        reqData.put("amount", amount);
        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(requestNo);
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);
        String result = "";
        try {
            result = XinWangUtil.serviceRequest(XinwangInterfaceName.CANCEL_PRE_TRANSACTION.getCode(), reqData);
        }catch (Exception e){
            logger.error(e);
        }
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);
        BaseResult baseResult = JSONObject.parseObject(result, BaseResult.class);
        if (baseResult == null||!baseResult.validate()) {
            logger.warn("取消冻结金额失败，requestNo=[{}],preTransactionNo=[{}]", requestNo, preTransactionNo);
            throw new XWTradeException(XWResponseCode.XW_CANCEL_PRE_TRANSACTION_FAIL);
        }
        return baseResult;
    }
}
