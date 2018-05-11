package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.redpacket.RedpacketCopyDao;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.config.XinWangConfig;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.trade.BizDetail;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.trade.XWSyncTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/5/18.
 */
@Service
public class XWSyncTransactionServiceImpl implements XWSyncTransactionService{

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    public static XinWangConfig CONFIG = ConfigFactory.create(XinWangConfig.class);

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    private RedpacketCopyDao redpacketDao;

    @Override
    public Map<String,Object> syncTransactionMarketing(Integer userId, String userRole, List<BigDecimal> amounts, BusinessType businessType, Integer orderId) throws APIException {
        //组装请求
        Map<String, Object> respMap = new HashMap<>();
        for(BigDecimal amount: amounts){
            String requestNo=XinWangUtil.createRequestNo();
            Date requestTime=new Date();
            Map<String,Object> reqData=new HashMap<>();
            String sourcePlatformUserNo = XinWangUtil.CONFIG.marketingAccount();
            String platformUserNo = userRole + userId;
            List details = new ArrayList<>();

            reqData.put("requestNo", requestNo);
            reqData.put("tradeType", XWTradeType.MARKETING.getCode());

            BizDetail detail = new BizDetail();
            detail.setBizType(XWBizType.MARKETING.getCode());
            detail.setAmount(amount);
            detail.setSourcePlatformUserNo(sourcePlatformUserNo);
            detail.setTargetPlatformUserNo(platformUserNo);
            details.add(detail);

            reqData.put("details", details);
            reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
            //create order
            XWRequest req=new XWRequest();
            req.setInterfaceName(XinwangInterfaceName.SYNC_TRANSACTION.getCode());
            req.setRequestNo(requestNo);
            req.setRequestTime(requestTime);
            req.setState(XWRequestState.DQR);
            req.setUserId(userId);
            req.setPlatformUserNo(platformUserNo);
            requestDao.createRequest(req);
            //保存请求参数
            XWResponseMessage requestParams = new XWResponseMessage();
            requestParams.setRequestNo(requestNo);
            requestParams.setRequestParams(JSON.toJSONString(reqData));
            requestDao.saveRequestMessage(requestParams);
            //发送请求
            String resultJson = "";
            try {
                resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.SYNC_TRANSACTION.getCode(),reqData);
            }catch (Exception e){
                LOG.error(e);
            }
            respMap = JSON.parseObject(resultJson);
            //save response msg
            XWResponseMessage responseParams = new XWResponseMessage();
            responseParams.setRequestNo(requestNo);
            responseParams.setResponseMsg(resultJson);
            requestDao.saveResponseMessage(responseParams);
            //处理结果
            String code = (String) respMap.get("code");
            String errorMessage = (String) respMap.get("errorMessage");
            if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
                int flowId = doSuccess(userId,sourcePlatformUserNo,amount, businessType,requestNo, orderId);
                respMap.put("flowId", flowId);
            }
            else{
                doFail(requestNo,errorMessage);
            }
        }
        return respMap;
    }

    @Override
    public Map<String,Object> syncTransactionMarketingForPlanSettle(Integer userId, String userRole, List<BigDecimal> amounts, BusinessType businessType, Integer orderId) throws APIException {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for(BigDecimal amount: amounts){
            totalAmount = totalAmount.add(amount);
        }

        // 新网平台营销账户
        XWFundAccount platformMarketingWLZH = accountDao.getFundAccount(SysCommonConsts.PLATFORM_USER_ID, SysFundAccountType.XW_PLATFORM_MARKETING_WLZH);
        LOG.info("计划结清使用红包，营销账户金额：" + platformMarketingWLZH.getAmount());
        LOG.info("计划结清使用红包，红包总金额：" + totalAmount);
        if(platformMarketingWLZH.getAmount().compareTo(totalAmount) < 0) {
            LOG.error("计划结清使用红包，营销账户余额不足，抛出异常：" + XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE.getCode());
            throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE.getCode());
        }

        //组装请求
        Map<String, Object> respMap = new HashMap<>();
        for(BigDecimal amount: amounts){
            String requestNo=XinWangUtil.createRequestNo();
            Date requestTime=new Date();
            Map<String,Object> reqData=new HashMap<>();
            String sourcePlatformUserNo = XinWangUtil.CONFIG.marketingAccount();
            String platformUserNo = userRole + userId;
            List details = new ArrayList<>();

            reqData.put("requestNo", requestNo);
            reqData.put("tradeType", XWTradeType.MARKETING.getCode());

            BizDetail detail = new BizDetail();
            detail.setBizType(XWBizType.MARKETING.getCode());
            detail.setAmount(amount);
            detail.setSourcePlatformUserNo(sourcePlatformUserNo);
            detail.setTargetPlatformUserNo(platformUserNo);
            details.add(detail);

            reqData.put("details", details);
            reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
            //create order
            XWRequest req=new XWRequest();
            req.setInterfaceName(XinwangInterfaceName.SYNC_TRANSACTION.getCode());
            req.setRequestNo(requestNo);
            req.setRequestTime(requestTime);
            req.setState(XWRequestState.DQR);
            req.setUserId(userId);
            req.setPlatformUserNo(platformUserNo);
            requestDao.createRequest(req);
            //保存请求参数
            XWResponseMessage requestParams = new XWResponseMessage();
            requestParams.setRequestNo(requestNo);
            requestParams.setRequestParams(JSON.toJSONString(reqData));
            requestDao.saveRequestMessage(requestParams);
            //发送请求
            String resultJson = "";
            try {
                resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.SYNC_TRANSACTION.getCode(),reqData);
            }catch (Exception e){
                LOG.error(e);
            }
            respMap = JSON.parseObject(resultJson);
            //save response msg
            XWResponseMessage responseParams = new XWResponseMessage();
            responseParams.setRequestNo(requestNo);
            responseParams.setResponseMsg(resultJson);
            requestDao.saveResponseMessage(responseParams);
            //处理结果
            String code = (String) respMap.get("code");
            String errorMessage = (String) respMap.get("errorMessage");
            if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
                int flowId = doSuccess(userId,sourcePlatformUserNo,amount, businessType,requestNo, orderId);
                respMap.put("flowId", flowId);
            }
            else{
                doFail(requestNo,errorMessage);
            }
        }
        return respMap;
    }

    private int doSuccess(Integer userId, String sourcePlatformUserNo, BigDecimal amount, BusinessType businessType, String requestNo, Integer orderId) throws APIException{
        XinwangAccount xinwangAccount=accountDao.getXinwangAccount(sourcePlatformUserNo);
        //获取平台角色
        String platformUserRole = xinwangAccount.getUserRole().getCode();
        String platformMarkingAccount = "XW_" + platformUserRole + "_WLZH";
        String userAccount = "XW_" + UserRole.INVESTOR.getCode() + "_WLZH";
        Map<String, Object> param1 = new HashMap<>();
        param1.put("accountName", xinwangAccount.getUserId());
        param1.put("accountType", platformMarkingAccount);
        Integer fenlibaoPlatformUserId = accountDao.getUserIdByAccountNameAndaAccountType(param1);
        int flowId = 0;

        Map<String, Object> m1 = new HashMap<>();
        m1.put("platformMarkingAccount", platformMarkingAccount);
        m1.put("amount", amount);
        m1.put("fenlibaoPlatformUserId", fenlibaoPlatformUserId);
        accountDao.updatePlatformMarketingAccount(m1);

        Map<String, Object> n1 = new HashMap<>();
        n1.put("platformMarkingAccount", platformMarkingAccount);
        n1.put("fenlibaoPlatformUserId", fenlibaoPlatformUserId);
        BigDecimal platformMarketingAmount = accountDao.getPlatformMarketingAccount(n1);

        Map<String, Object> o1 = new HashMap<>();
        o1.put("userId", userId);
        o1.put("businessType", userAccount);
        Integer userAccountId = accountDao.getAccountIdByUserId(o1);

        Map<String, Object> o2 = new HashMap<>();
        o2.put("accountName",  xinwangAccount.getUserId());
        o2.put("accountType", platformMarkingAccount);
        Integer markingAccountId = accountDao.getAccountIdByAccountNameAndaAccountType(o2);
        // 插入平台资金交易记录，内部转账(转出)
        addTrunoutFundsRecord(markingAccountId, userAccountId, businessType.getCode(), amount, platformMarketingAmount, businessType.getName(), orderId);

        Map<String, Object> m2 = new HashMap<>();
        m2.put("userId", userId);
        m2.put("amount", amount);
        m2.put("businessType", userAccount);
        accountDao.updatePTAccountWLZH(m2);

        Map<String, Object> n2 = new HashMap<>();
        n2.put("userId", userId);
        n2.put("userAccount", userAccount);
        BigDecimal PTAccountWLZHAmount = accountDao.getPTAccountWLZH(n2);
        // 插入个人资金交易记录，内部转账(转入)
        flowId = addTruninFundsRecord(userAccountId, markingAccountId, businessType.getCode(), amount, PTAccountWLZHAmount, businessType.getName(), orderId);

        //请求成功
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.CG);
        requestDao.updateRequest(param);

        return flowId;
    }

    private void doFail(String requestNo, String errorMessage) throws APIException{
        //请求失败或处理失败
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.SB);
        requestDao.updateRequest(param);
        String errorLog=String.format(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getMessage(),errorMessage);
        LOG.error(errorLog);
        //throw new XWTradeException(XWResponseCode.XW_SYNC_TRANSACTION_WRONG.getCode(),errorLog);
    }

    public int addTruninFundsRecord(int truninAccountId, int trunoutAccountId, int tradeTypeId, BigDecimal payinAmount, BigDecimal balance, String remark, Integer orderId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("truninAccountId", truninAccountId);
        paramMap.put("trunoutAccountId", trunoutAccountId);
        paramMap.put("tradeTypeId", tradeTypeId);
        paramMap.put("payinAmount", payinAmount);
        paramMap.put("balance", balance);
        paramMap.put("remark", remark);
        paramMap.put("orderId", orderId);
        return redpacketDao.addTruninFundsRecord(paramMap);
    }

    public int addTrunoutFundsRecord(int truninAccountId, int trunoutAccountId, int tradeTypeId, BigDecimal payoutAmount, BigDecimal balance, String remark, Integer orderId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("truninAccountId", truninAccountId);
        paramMap.put("trunoutAccountId", trunoutAccountId);
        paramMap.put("tradeTypeId", tradeTypeId);
        paramMap.put("payoutAmount", payoutAmount);
        paramMap.put("balance", balance);
        paramMap.put("remark", remark);
        paramMap.put("orderId", orderId);
        return redpacketDao.addTrunoutFundsRecord(paramMap);
    }
}
