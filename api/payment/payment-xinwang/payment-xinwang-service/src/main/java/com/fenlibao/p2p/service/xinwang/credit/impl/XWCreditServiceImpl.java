package com.fenlibao.p2p.service.xinwang.credit.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.credit.BaseCreditInfo;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysTransferInfo;
import com.fenlibao.p2p.model.xinwang.entity.credit.XWTransferResult;
import com.fenlibao.p2p.model.xinwang.entity.trade.PreTransactionResult;
import com.fenlibao.p2p.model.xinwang.entity.trade.SyncTransactionResult;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.transaction.PreTransactionParam;
import com.fenlibao.p2p.model.xinwang.param.transaction.SyncTransactionParam;
import com.fenlibao.p2p.service.xinwang.credit.SysCreditService;
import com.fenlibao.p2p.service.xinwang.credit.XWCreditService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.trade.XWTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @date 2017/5/31 15:38
 */
@Service
public class XWCreditServiceImpl implements XWCreditService {
    protected final Logger LOG = LogManager.getLogger(this.getClass());
    @Resource
    private SysCreditService sysCreditService;
    @Resource
    private XWTransactionService transactionService;
    @Resource
    private XWRequestDao requestDao;
    @Resource
    private SysOrderService orderService;

    static String commonReason = "存管接口调用失败";

    @Override
    public void doDebentureSale(int userId, int creditId, String requestNo) throws Exception {
        BaseCreditInfo creditInfo = sysCreditService.getBaseCreditInfo(creditId);
        if (creditInfo == null) {
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        XWTransferResult transferResult = transfer(userId, creditInfo.getBidId(), creditInfo.getSourceAmount(), requestNo);
        if (transferResult == null || !transferResult.validate()) {
            LOG.warn("用户债权转让失败，requestNo=[{}],userId=[{}]", transferResult.getRequestNo(), userId);
            throw new XWTradeException(XWResponseCode.XW_ZQZR_FAIL.getCode(), String.format(XWResponseCode.XW_ZQZR_FAIL.getMessage(), transferResult.getErrorMessage()));
        }
    }

    @Override
    public void doCancelDebentureSale(Integer creditId) throws Exception {
        SysTransferInfo transferInfo = sysCreditService.getTransferInfo(creditId);
        if (transferInfo == null) {
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        BaseResult cancelResult = cancelTransfer(transferInfo.getCreditsaleNo());
        if (cancelResult == null || !cancelResult.validate()) {
            LOG.warn("用户取消债权转让失败，requestNo=[{}]", transferInfo.getCreditsaleNo());
            throw new XWTradeException(XWResponseCode.XW_ZQZR_CANCEL_FAIL.getCode(), String.format(XWResponseCode.XW_ZQZR_CANCEL_FAIL.getMessage(), cancelResult.getErrorMessage()));
        }
    }

    @Override
    public void doCreditAssignment(int orderId, BigDecimal zrCommission, BigDecimal srCommission){
        SysTransferInfo transferInfo = sysCreditService.getInfoByOrder(orderId);
        //非存管版本没有requestNo字段
        if (transferInfo == null || StringUtils.isBlank(transferInfo.getCreditsaleNo())) {
            return;
        }
        //预处理
        String transactionNo = XinWangUtil.createRequestNo();
        PreTransactionParam param = new PreTransactionParam();
        param.setAmount(transferInfo.getCreditAmount());//冻结金额
        param.setBizType(XWBizType.CREDIT_ASSIGNMENT);
        param.setProjectNo(transferInfo.getBidId() + "");
        param.setPlatformUserNo(UserRole.INVESTOR.getCode() + transferInfo.getUserId());
        param.setCreditsaleRequestNo(transferInfo.getCreditsaleNo());
        param.setShare(transferInfo.getSourceAmount());
        param.setRequestNo(transactionNo);
        PreTransactionResult transactionResult;
        //create transaction order
        XWRequest transactionRequest = new XWRequest(transactionNo);
        transactionRequest.setPlatformUserNo(UserRole.INVESTOR.getCode() + transferInfo.getUserId());
        transactionRequest.setOrderId(orderId);
        transactionRequest.setRequestTime(new Date());
        transactionRequest.setInterfaceName(XinwangInterfaceName.USER_AUTO_PRE_TRANSACTION.getCode());
        transactionRequest.setUserId(transferInfo.getUserId());
        transactionRequest.setState(XWRequestState.DQR);
        requestDao.createRequest(transactionRequest);
        try {
            transactionResult = transactionService.preTransaction(param);
        } catch (Exception e) {
            transactionRequest.setState(XWRequestState.SB);
            requestDao.updateRequest(transactionRequest);
            LOG.warn(String.format("新网用户交易预处理失败,orderId:[{}]", orderId));
            throw new XWTradeException(XWResponseCode.XW_PRE_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_PRE_TRANSACTION_WRONG.getMessage(), commonReason));
        }
        if (!transactionResult.validate()) {
            transactionRequest.setState(XWRequestState.SB);
            requestDao.updateRequest(transactionRequest);
            LOG.warn(String.format("新网用户交易预处理失败,orderId:[{}]", orderId));
            throw new XWTradeException(XWResponseCode.XW_PRE_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_PRE_TRANSACTION_WRONG.getMessage(), transactionResult.getErrorMessage()));
        }
        //预处理成功
        transactionRequest.setState(XWRequestState.CG);
        requestDao.updateRequest(transactionRequest);
        //单笔交易
        SyncTransactionParam syncParam = new SyncTransactionParam();
        syncParam.setAmount(transferInfo.getCreditAmount());//本息和（t6260.F03转让价格）
        syncParam.setProjectNo(String.valueOf(transferInfo.getBidId()));
        syncParam.setBizType(XWBizType.CREDIT_ASSIGNMENT);
        syncParam.setTargetPlatformUserNo(UserRole.INVESTOR.getCode() + transferInfo.getDebtUserId());
        syncParam.setSourcePlatformUserNo(UserRole.INVESTOR.getCode() + transferInfo.getUserId());
        syncParam.setTradeType(XWTradeType.CREDIT_ASSIGNMENT);
        syncParam.setShare(transferInfo.getSourceAmount());//本金
        syncParam.setSaleRequestNo(transferInfo.getCreditsaleNo());
        syncParam.setFreezeRequestNo(transactionResult.getRequestNo());
        Throwable throwable = null;
        try {
            this.doSyncTransaction(transferInfo.getUserId(), orderId, syncParam, zrCommission, srCommission);
        } catch (Exception ex) {
            try {
                transactionService.cancelTransaction(transactionNo, transferInfo.getCreditAmount());
            } catch (Throwable th) {
                throwable = th;
            }
            LOG.warn(String.format("新网用户债权认购失败,orderId:[{}]，已经解冻资金", orderId));
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getMessage(), ""));
        }finally {
            if (throwable != null) {
                //如果失败的话写入失败订单
                StringBuilder str = new StringBuilder();
                str.append("新网债权认购回滚失败，预处理流水：").append(transactionNo)
                        .append(",orderId:").append(orderId)
                        .append(":").append(throwable.getMessage());
                orderService.insertOrderExceptionLog(orderId, str.toString());
            }
        }
    }

    @Override
    public void doCancelCreditAssignment(int orderId) {
        SysTransferInfo transferInfo = sysCreditService.getInfoByOrder(orderId);
        if (null!=transferInfo&&StringUtils.isBlank(transferInfo.getTransactionNo())) {
            return;
        }
        BaseResult result = transactionService.cancelTransaction(transferInfo.getTransactionNo(), transferInfo.getCreditAmount());
        if (result.getCode().equals(GeneralResponseCode.FAIL.getCode()) || result.getStatus().equals(GeneralStatus.INIT.getStatus())) {
            LOG.warn("取消冻结金额失败，orderId=[{}]", orderId);
            throw new XWTradeException(XWResponseCode.XW_CANCEL_PRE_TRANSACTION_FAIL.getCode(), String.format(XWResponseCode.XW_CANCEL_PRE_TRANSACTION_FAIL.getMessage(), result.getErrorMessage()));
        }
    }

    @Override
    public BaseResult cancelTransfer(String sourceRequestNo) throws Exception {
        Map<String, Object> reqData = new HashMap<String, Object>();
        String requestNo = XinWangUtil.createRequestNo();
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        reqData.put("requestNo", requestNo);
        reqData.put("creditsaleRequestNo", sourceRequestNo);
        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(requestNo);
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);

        String result;
        try {
            result = XinWangUtil.serviceRequest(XinwangInterfaceName.CANCEL_DEBENTURE_SALE.getCode(), reqData);
        } catch (Exception e) {
            throw new XWTradeException(XWResponseCode.XW_ZQZR_CANCEL_FAIL.getCode(),String.format(XWResponseCode.XW_ZQZR_CANCEL_FAIL.getMessage(),commonReason));
        }
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);

        BaseResult transferResult = JSONObject.parseObject(result, BaseResult.class);
        if (transferResult == null || !transferResult.validate()) {
            LOG.warn("用户债权转让失败，requestNo=[{}]", requestNo);
            throw new XWTradeException(XWResponseCode.XW_ZQZR_CANCEL_FAIL.getCode(), String.format(XWResponseCode.XW_ZQZR_CANCEL_FAIL.getMessage(), commonReason));
        }
        return transferResult;
    }

    private XWTransferResult transfer(int userId, Integer bidId, BigDecimal saleShare, String requestNo) throws Exception {
        Map<String, Object> reqData = new HashMap<String, Object>();
        String platformUserNo = UserRole.INVESTOR.getCode() + userId;
        String projectNo = bidId + "";
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("saleShare", saleShare);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        reqData.put("requestNo", requestNo);
        reqData.put("projectNo", projectNo);
        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(requestNo);
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);

        String result;
        try {
            result = XinWangUtil.serviceRequest(XinwangInterfaceName.DEBENTURE_SALE.getCode(), reqData);
        } catch (Exception e) {
            throw new XWTradeException(XWResponseCode.XW_ZQZR_FAIL);
        }
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);
        XWTransferResult transferResult = JSONObject.parseObject(result, XWTransferResult.class);
        if (transferResult == null ) {
            throw new XWTradeException(XWResponseCode.XW_ZQZR_FAIL.getCode(),String.format(XWResponseCode.XW_ZQZR_FAIL.getMessage(),commonReason));
        }
        return transferResult;
    }

    public void doSyncTransaction(Integer userId, Integer orderId, SyncTransactionParam param, BigDecimal zrCommission, BigDecimal srCommission){
        String requestNo = XinWangUtil.createRequestNo();
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("tradeType", param.getTradeType().getCode());
        reqData.put("projectNo", param.getProjectNo());
        reqData.put("requestNo", requestNo);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(new Date()));
        reqData.put("saleRequestNo", param.getSaleRequestNo());

        List<Object> details=new ArrayList<Object>();
        Map<String, Object> detailsItem = new HashMap<String, Object>();
        //债权转让信息
        detailsItem.put("bizType", param.getBizType().getCode());
        detailsItem.put("freezeRequestNo", param.getFreezeRequestNo());
        detailsItem.put("sourcePlatformUserNo", param.getSourcePlatformUserNo());
        detailsItem.put("targetPlatformUserNo", param.getTargetPlatformUserNo());//标借款人
        detailsItem.put("amount", param.getAmount());
        detailsItem.put("share", param.getShare());
        details.add(detailsItem);
        //受让方佣金
        if (srCommission.compareTo(BigDecimal.ZERO) > 0) {
            detailsItem = new HashMap<>();
            detailsItem.put("bizType", XWBizType.COMMISSION.getCode());
            detailsItem.put("freezeRequestNo", param.getFreezeRequestNo());
            detailsItem.put("sourcePlatformUserNo", param.getSourcePlatformUserNo());
            detailsItem.put("amount", srCommission);
            details.add(detailsItem);
        }
        //出让方佣金
        if (zrCommission.compareTo(BigDecimal.ZERO) > 0) {
            detailsItem = new HashMap<>();
            detailsItem.put("bizType", XWBizType.COMMISSION.getCode());
            detailsItem.put("freezeRequestNo", param.getFreezeRequestNo());
            detailsItem.put("sourcePlatformUserNo", param.getTargetPlatformUserNo());
            detailsItem.put("amount", zrCommission);
            details.add(detailsItem);
        }
        reqData.put("details", details);
        //create transaction order
        XWRequest syncRequest = new XWRequest(requestNo);
        syncRequest.setPlatformUserNo(param.getSourcePlatformUserNo());
        syncRequest.setOrderId(orderId);
        syncRequest.setRequestTime(new Date());
        syncRequest.setInterfaceName(XinwangInterfaceName.SYNC_TRANSACTION.getCode());
        syncRequest.setUserId(userId);
        syncRequest.setState(XWRequestState.DQR);
        requestDao.createRequest(syncRequest);
        //保存请求参数
        XWResponseMessage requestMessage = new XWResponseMessage();
        requestMessage.setRequestNo(requestNo);
        requestMessage.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestMessage);
        String result = "";
        try {
            result = XinWangUtil.serviceRequest(XinwangInterfaceName.SYNC_TRANSACTION.getCode(), reqData);
        } catch (APIException ex) {
            syncRequest.setState(XWRequestState.SB);
            requestDao.updateRequest(syncRequest);
            LOG.warn("单笔交易失败，requestNo=[{}],platformUserNo=[{}]", requestNo, param.getSourcePlatformUserNo());
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG);
        } catch (Exception e){
            LOG.error(e);
        }
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(result);
        requestDao.saveResponseMessage(responseParams);
        SyncTransactionResult transactionInfo = JSONObject.parseObject(result, SyncTransactionResult.class);
        if (transactionInfo == null || !transactionInfo.validate()) {
            syncRequest.setState(XWRequestState.SB);
            requestDao.updateRequest(syncRequest);
            LOG.warn(String.format("新网用户单笔交易失败,orderId:[{}]", orderId));
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getMessage(), transactionInfo==null?"":transactionInfo.getErrorMessage()));
        }
    }
}
