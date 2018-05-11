package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.order.SysRechargeManageDao;
import com.fenlibao.p2p.dao.xinwang.pay.XWRechargeDao;
import com.fenlibao.p2p.model.xinwang.config.XinWangConfig;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.order.RechargeOrderEntity;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWDirectRecharge;
import com.fenlibao.p2p.model.xinwang.entity.query.TransactionQueryInfo;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.entity.trade.SyncTransactionResult;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.PayConpany;
import com.fenlibao.p2p.model.xinwang.enums.PaymentMode;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;
import com.fenlibao.p2p.model.xinwang.enums.common.PayConst;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.model.xinwang.param.transaction.SyncTransactionParam;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.pay.XWRechargeProcessService;
import com.fenlibao.p2p.service.xinwang.query.XWQueryTransactionService;
import com.fenlibao.p2p.service.xinwang.trade.XWRechargeService;
import com.fenlibao.p2p.service.xinwang.trade.XWTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2017/5/11 13:45
 */
@Service
public class XWRechargeServiceImpl implements XWRechargeService {
    protected Logger logger = LogManager.getLogger(XWRechargeService.class);

    @Resource
    private XWUserInfoService userInfoService;
    @Resource
    private XWRechargeDao rechargeDao;
    @Resource
    private SysRechargeManageDao sysRechargeManageDao;
    @Resource
    private SysOrderManageDao orderManageDao;
    @Resource
    private XWRequestDao requestDao;
    @Resource
    private PTCommonDao ptCommonDao;
    @Resource
    private XWAccountDao accountDao;
    @Resource
    private XWRechargeProcessService rechargeProcessService;
    @Resource
    private XWTransactionService transactionService;
    @Resource
    private XWQueryTransactionService queryTransactionService;
    public static XinWangConfig CONFIG = ConfigFactory.create(XinWangConfig.class);

    @Override
    public Map<String, Object> doRecharge(int userId, UserRole userRole, String uri, int orderId, String requestNo, PaymentMode paymode, String bankcode) throws XWTradeException {
        String platformUserNo = userRole.getCode() + userId;
        //特殊账户编码
        int specialId = ptCommonDao.getSpecialUserId();
        if (specialId == userId) {
            XinwangAccount account = accountDao.getXWRoleAccount(userId, userRole);
            platformUserNo = account.getPlatformUserNo();
        }
        Date requestTime = new Date();
        XinwangUserInfo userInfo = userInfoService.queryUserInfo(platformUserNo);
        RechargeOrderEntity rechargeOrder = sysRechargeManageDao.getOrder(orderId);
        SystemOrder order = orderManageDao.get(orderId,true);
        if (rechargeOrder == null) {
            logger.warn("充值订单不存在，userId=[{}],orderId=[{}]", userId, orderId);
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        if (!XWOrderStatus.DTJ.equals(order.getOrderStatus())) {
            logger.warn("充值订单状态不正确，userId=[{}],orderId=[{}]", userId, orderId);
            throw new XWTradeException(XWResponseCode.COMMON_ORDER_STATUS_WRONG);
        }
        //组装请求
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+uri);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("amount", rechargeOrder.F03);
        reqData.put("expectPayCompany", PayConpany.parse(CONFIG.payCompanyChannel()));
        reqData.put("rechargeWay", paymode.getCode());
        if (StringUtils.isEmpty(bankcode)) {
            reqData.put("bankcode", userInfo.getBankcode());
        }else{
            reqData.put("bankcode", bankcode);
        }
        DateTime dateTime = new DateTime();
        reqData.put("expired", dateTime.plusMinutes(30).toString("yyyyMMddHHmmss"));
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        Map<String,Object> sendData;
        try {
            sendData = XinWangUtil.gatewayRequest(XinwangInterfaceName.RECHARGE.getCode(),reqData);
        } catch (Exception e) {
            logger.warn("用户新网存管充值组装参数异常：userId:[{}],requestNo:[{}]", userId, requestNo);
            throw new XWTradeException(XWResponseCode.XW_ASSEMBLE_REQUEST_PARAM_WRONG);
        }
        //组装参数没有报错才会创建新网订单
        XWRequest request = new XWRequest();
        request.setPlatformUserNo(platformUserNo);
        request.setInterfaceName(XinwangInterfaceName.RECHARGE.getCode());
        request.setOrderId(orderId);
        request.setRequestNo(requestNo);
        request.setState(XWRequestState.DTJ);
        request.setRequestTime(requestTime);
        request.setUserId(userId);
        requestDao.createRequest(request);
        rechargeDao.insertRechargeRequest(reqData);
        return sendData;
    }

    /**
     * 网关充值回调
     * @param respMap
     * @throws Exception
     */
    @Override
    public void handleNotify(Map<String, Object> respMap) throws Exception {
        String platformUserNo = (String) respMap.get("platformUserNo");
        String code = (String) respMap.get("code");
        String status = (String) respMap.get("status");
        String rechargeStatus = (String) respMap.get("rechargeStatus");
        String requestNo = (String) respMap.get("requestNo");
        XWRequest request = requestDao.getByRequestNo(requestNo);
        SystemOrder order = orderManageDao.get(request.getOrderId(),true);//lock up
        if (order != null && order.getOrderStatus().equals(XWOrderStatus.DTJ)) {
            order.setOrderStatus(XWOrderStatus.DQR);
            rechargeProcessService.rechargeAccept(request);
        }
        if ( GeneralResponseCode.FAIL.equals(code) || GeneralStatus.INIT.equals(status)) {//充值失败
            String errorCode = (String) respMap.get("errorCode");
            String errorMessage = (String) respMap.get("errorMessage");
            logger.info("新网充值失败，userId=[{}],errorCode=[{}],errorMessage=[{}]", platformUserNo, errorCode, errorMessage);
            rechargeProcessService.rechargeFail(request);
            return;
        }
        //成功之后保存到资金账户
        if (order != null && order.getOrderStatus().equals(XWOrderStatus.DQR) ) {
            if (PayConst.FAIL.equals(rechargeStatus)) {
                rechargeProcessService.rechargeFail(request);
                return;
            } else if (PayConst.PENDDING.equals(rechargeStatus)) {
                rechargeProcessService.rechargeAccept(request);
                return;
            } else if (PayConst.SUCCESS.equals(rechargeStatus)) {
                BusinessType businessType = new BusinessType();
                businessType.setCode(SysTradeFeeCode.CZ);
                businessType.setStatus("QY");
                businessType.setName("充值");
                rechargeProcessService.rechargeSuccess(request,order, businessType);
            }
        }
    }

    @Override
    public XWDirectRecharge doDirectRecharge(int userId, UserRole userRole,int orderId, String requestNo) {
        String platformUserNo = userRole.getCode() + userId;
        Date requestTime = new Date();
        RechargeOrderEntity rechargeOrder = sysRechargeManageDao.getOrder(orderId);
        SystemOrder order = orderManageDao.get(orderId,true);
        if (rechargeOrder == null) {
            logger.warn("充值订单不存在，userId=[{}],orderId=[{}]", userId, orderId);
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        if (!XWOrderStatus.DTJ.equals(order.getOrderStatus())) {
            logger.warn("充值订单状态不正确，userId=[{}],orderId=[{}]", userId, orderId);
            throw new XWTradeException(XWResponseCode.COMMON_ORDER_STATUS_WRONG);
        }
        Map<String, Object> reqData = new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("amount", rechargeOrder.F03);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("requestNo", requestNo);
        reqData.put("expectPayCompany", PayConpany.parse(CONFIG.payCompanyChannel()));
        reqData.put("rechargeWay", PaymentMode.PROXY);
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        //组装参数没有报错才会创建新网订单
        XWRequest request = new XWRequest();
        request.setPlatformUserNo(platformUserNo);
        request.setInterfaceName(XinwangInterfaceName.RECHARGE.getCode());
        request.setRequestNo(requestNo);
        request.setState(XWRequestState.DTJ);
        request.setRequestTime(requestTime);
        requestDao.createRequest(request);
        rechargeDao.insertRechargeRequest(reqData);
        String result;
        try {
            result = XinWangUtil.serviceRequest(XinwangInterfaceName.DIRECT_RECHARGE.getCode(), reqData);
        } catch (Exception e) {
            logger.warn(String.format("直连充值失败，userId:[{}],requestNo:[{}]", userId, requestNo));
            throw new XWTradeException(XWResponseCode.COMMON_REQUEST_FAIL);
        }
        XWDirectRecharge rechargeResult = JSONObject.parseObject(result, XWDirectRecharge.class);
        if (!rechargeResult.validate()) {
            logger.warn(String.format("直连充值失败，userId:[{}],requestNo:[{}]", userId, requestNo));
           throw new XWTradeException(XWResponseCode.PAYMENT_TOPUP_FAIL);
        }
        return rechargeResult;
    }

    @Override
    public int doAlternativeRecharge(int userId, String platformUserNo, BigDecimal amount, BusinessType businessType) {
        //创建系统订单
        String requestNo = XinWangUtil.createRequestNo();
        String sourceUser = XinWangUtil.CONFIG.replacementRechargeAccount();
        Date currentTime = new Date();
        int orderId = rechargeProcessService.addBackstageOrder(userId, amount, requestNo, Source.HT);
        SyncTransactionParam param = new SyncTransactionParam();
        param.setAmount(amount);
        param.setSourcePlatformUserNo(sourceUser);
        param.setTargetPlatformUserNo(platformUserNo);
        param.setRemark("线下充值");
        param.setTradeType(XWTradeType.MARKETING);
        param.setBizType(XWBizType.ALTERNATIVE_RECHARGE);
        //create xw order
        XWRequest request = new XWRequest();
        request.setOrderId(orderId);
        request.setRequestNo(requestNo);
        request.setPlatformUserNo(platformUserNo);
        request.setUserId(userId);
        request.setState(XWRequestState.DTJ);
        request.setInterfaceName(XinwangInterfaceName.SYNC_TRANSACTION.getCode());
        request.setRequestTime(currentTime);
        request.setOrderId(orderId);
        requestDao.createRequest(request);
        SyncTransactionResult transactionResult = null;
        try {
            transactionResult = transactionService.doSyncTransaction(param);
        } catch (Exception e) {
            rechargeProcessService.rechargeFail(request);
            logger.warn("单笔交易失败，requestNo=[{}],platformUserNo=[{}]", requestNo, param.getSourcePlatformUserNo());
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getMessage(), "新网接口调用失败"));
        }
        if (!transactionResult.validate()) {
            rechargeProcessService.rechargeFail(request);
            logger.warn("单笔交易失败，requestNo=[{}],platformUserNo=[{}]", requestNo, param.getSourcePlatformUserNo());
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getMessage(), transactionResult.getErrorMessage()));
        }
        //代充值受理成功
//        rechargeProcessService.rechargeAccept(request);
        //代充值成功
//        SystemOrder systemOrder = orderManageDao.get(orderId,true);//lock up
        rechargeProcessService.alternativeRechargeSuccess(request, orderId, businessType);
        return request.getId();
    }

    @Override
    public int doFundsTransfer(String sourceUserNo, String targetUserNo, BigDecimal amount, BusinessType businessType) {
        //平台账号
        int userId = ptCommonDao.getSpecialUserId();
        String requestNo = XinWangUtil.createRequestNo();
        Date currentTime = new Date();
        int orderId = rechargeProcessService.addBackstageOrder(userId, amount, requestNo, Source.HT);
        SyncTransactionParam param = new SyncTransactionParam();
        param.setAmount(amount);
        param.setSourcePlatformUserNo(sourceUserNo);
        param.setTargetPlatformUserNo(targetUserNo);
        param.setRemark("划拨");
        param.setTradeType(XWTradeType.FUNDS_TRANSFER);
        param.setBizType(XWBizType.FUNDS_TRANSFER);
        //create xw order
        XWRequest xwOrder = new XWRequest();
        xwOrder.setOrderId(orderId);
        xwOrder.setRequestNo(requestNo);
        xwOrder.setPlatformUserNo(sourceUserNo);
        xwOrder.setUserId(userId);
        xwOrder.setState(XWRequestState.DTJ);
        xwOrder.setInterfaceName(XinwangInterfaceName.SYNC_TRANSACTION.getCode());
        xwOrder.setRequestTime(currentTime);
        requestDao.createRequest(xwOrder);
        SyncTransactionResult transactionResult = null;
        XWRequest request = requestDao.getByRequestNo(requestNo);
        request.setRequestNo(requestNo);
        request.setOrderId(orderId);
        try {
            transactionResult = transactionService.doSyncTransaction(param);
        } catch (Exception e) {
            rechargeProcessService.rechargeFail(request);
            logger.warn("单笔交易失败，requestNo=[{}],platformUserNo=[{}]", requestNo, param.getSourcePlatformUserNo());
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getMessage(), "新网接口调用失败"));
        }
        if (!transactionResult.validate()) {
            rechargeProcessService.rechargeFail(request);
            logger.warn("单笔交易失败，requestNo=[{}],platformUserNo=[{}]", requestNo, param.getSourcePlatformUserNo());
            throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getCode(), String.format(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getMessage(), transactionResult.getErrorMessage()));
        }
        //受理成功
//        rechargeProcessService.rechargeAccept(request);
        rechargeProcessService.fundsTransferSuccess(sourceUserNo, targetUserNo, request, businessType,amount);
        return request.getId();
    }


    @Override
    public List<String> getOrderNeedComfired(XinwangInterfaceName interfaceName, Date requestTime) {
        return requestDao.getOrderNeedComfired(interfaceName,requestTime);
    }

    @Override
    public void comfiredOrder(String requestNo) {
        XWRequest request = requestDao.getByRequestNo(requestNo);
        SystemOrder order;
        //没有系统订单的新网请求，默认为是脏数据
        if (request.getOrderId() == null) {
            request.setState(XWRequestState.SB);
            requestDao.updateRequest(request);
            return;
        }
        order = orderManageDao.get(request.getOrderId(),true);
        if (order != null && order.getOrderStatus().equals(XWOrderStatus.DTJ)) {
            order.setOrderStatus(XWOrderStatus.DQR);
            rechargeProcessService.rechargeAccept(request);
        }
        if (XWOrderStatus.DQR.equals(order.getOrderStatus())) {
            String resultJson;
            try {
                resultJson = queryTransactionService.queryTransaction(null, null, XinwangInterfaceName.RECHARGE.getCode(), requestNo);
            } catch (Exception e) {
                throw new XWTradeException(XWResponseCode.XW_QUERY_TRANSACTION_WRONG.getCode(),String.format(XWResponseCode.XW_QUERY_TRANSACTION_WRONG.getMessage(),"新网接口调用失败"));
            }
            TransactionQueryInfo info = JSONObject.parseObject(resultJson, TransactionQueryInfo.class);
            if (!info.validate()) {
                rechargeProcessService.rechargeFail(request);
                throw new XWTradeException(XWResponseCode.XW_QUERY_TRANSACTION_WRONG.getCode(),String.format(XWResponseCode.XW_QUERY_TRANSACTION_WRONG.getMessage(),info.getErrorMessage()));
            }
            //处理本地信息
            List<Map<String, Object>> list = info.getRecords();
            if (list != null) {
                list = info.getRecords();
                String rechargeStatus = (String) list.get(0).get("status");
                if (PayConst.FAIL.equals(rechargeStatus)) {
                    rechargeProcessService.rechargeFail(request);
                    logger.info("充值失败：requestNo:"+requestNo);
                } else if (PayConst.PENDDING.equals(rechargeStatus)) {
                    logger.info("充值状态为待确认：requestNo:"+requestNo);
                } else {
                    if (PayConst.SUCCESS.equals(rechargeStatus)) {
                        BusinessType businessType = new BusinessType();
                        businessType.setCode(SysTradeFeeCode.CZ);
                        businessType.setStatus("QY");
                        businessType.setName("充值");
                        rechargeProcessService.rechargeSuccess(request, order, businessType);
                        logger.info("充值成功：requestNo:" + requestNo);
                    }
                }
            }

        }
    }
}
