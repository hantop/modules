package com.fenlibao.p2p.service.xinwang.pay.impl;

import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.order.SysRechargeManageDao;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.order.RechargeOrderEntity;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.pay.SysPaymentLimitVO;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWFundsTransfer;
import com.fenlibao.p2p.model.xinwang.entity.trade.BusinessType;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.order.XWTradeOrderType;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysPaymentInstitution;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.pay.XWFundsTransferService;
import com.fenlibao.p2p.service.xinwang.pay.XWRechargeProcessService;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2017/5/23 18:04
 */
@Service
public class XWRechargeProcessServiceImpl implements XWRechargeProcessService {
    protected Logger logger = LogManager.getLogger(XWRechargeProcessService.class);

    @Resource
    SysOrderManageDao orderManageDao;
    @Resource
    SysRechargeManageDao rechargeManageDao;
    @Resource
    XWAccountDao accountDao;
    @Resource
    XWRequestDao requestDao;
    @Resource
    PTCommonDao ptCommonDao;
    @Resource
    XWUserInfoService userInfoService;
    @Resource
    SysOrderService sysOrderService;
    @Resource
    private XWFundsTransferService fundsTransferService;

    @Override
    public int addOrder(int userId, UserRole userRole, BigDecimal amount, String requestNo, Source source) {
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new XWTradeException(XWResponseCode.COMMON_PARAM_TYPE_WRONG); //金额或支付类型错误
        }
        //充值限额校验
        validateCurdateTotalAmount(userId,userRole, amount);
        return addBackstageOrder(userId, amount, requestNo, source);
    }

    @Transactional
    @Override
    public int addBackstageOrder(int userId, BigDecimal amount, String requestNo, Source source) {
        /*
        添加订单
         */
        SystemOrder systemOrder = new SystemOrder();
        systemOrder.typeCode = XWTradeOrderType.CHARGE.orderType();
        systemOrder.orderStatus = XWOrderStatus.DTJ;
        systemOrder.source = source;
        systemOrder.userId = userId;
        systemOrder.flowNo = requestNo;
        orderManageDao.add(systemOrder);
        /*
        添加充值订单
         */
        RechargeOrderEntity rechargeOrder = new RechargeOrderEntity();
        rechargeOrder.F01 = systemOrder.id;
        rechargeOrder.F02 = userId;
        rechargeOrder.F03 = amount;
        //f04,f05 手续费，不收
        rechargeOrder.F07 = SysPaymentInstitution.XW.getCode();
        rechargeManageDao.addOrder(rechargeOrder);
        return systemOrder.id;
    }

    @Override
    public void validateCurdateTotalAmount(int userId,UserRole userRole, BigDecimal amount) {
        BigDecimal minAmount = BigDecimal.TEN;//最低充值金额
        if (minAmount.compareTo(amount) > 0) {
            throw new XWTradeException(XWResponseCode.PAYMENT_RECHARGE_LIMIT_LOW.getCode(),
                    String.format(XWResponseCode.PAYMENT_RECHARGE_LIMIT_LOW.getMessage(), minAmount.toBigInteger()));
        }
        XinwangAccount ptAccount = accountDao.getXWRoleAccount(userId, userRole);
        if (null == ptAccount) {
            throw new XWTradeException(XWResponseCode.USER_XW_ACCOUNT_NOT_EXIST);
        }
        List<SysPaymentLimitVO> paymentLimits = rechargeManageDao.getLimitList(ptAccount.getBankcode());
        if (null != paymentLimits && paymentLimits.size() > 0) {
            BigDecimal singleLimit = paymentLimits.get(0).getSingleLimit();//单位：万元
            BigDecimal dailyLimit = paymentLimits.get(0).getDailyLimit();//单位：万元
            BigDecimal curdateTotalAmount = rechargeManageDao.getCurdateTotalAmount(userId, SysPaymentInstitution.XW.getCode());
            if (amount.compareTo(singleLimit.multiply(new BigDecimal(10000))) > 0) {
                throw new XWTradeException(XWResponseCode.PAYMENT_RECHARGE_LIMIT_EXCEED);
            }

            BigDecimal todayRechargeTotal = curdateTotalAmount.add(amount);
            if (todayRechargeTotal.compareTo(dailyLimit.multiply(new BigDecimal(10000))) > 0) {
                throw new XWTradeException(XWResponseCode.PAYMENT_RECHARGE_LIMIT_EXCEED_CURDATE);
            }
        }
    }

    @Transactional
    public void rechargeAccept(XWRequest xwRequest) {
        sysOrderService.submit(xwRequest.getOrderId());
        xwRequest.setState(XWRequestState.DQR);
        requestDao.updateRequest(xwRequest);
    }

    @Transactional
    public void rechargeFail(XWRequest xwRequest) {
        sysOrderService.fail(xwRequest.getOrderId());
        xwRequest.setState(XWRequestState.SB);
        requestDao.updateRequest(xwRequest);
    }

    @Transactional
    public void alternativeRechargeSuccess(XWRequest xwRequest,int orderId, BusinessType businessType) {
        XWRequest request = requestDao.getByRequestNo(xwRequest.getRequestNo());
        RechargeOrderEntity rechargeOrder = rechargeManageDao.getOrder(request.getOrderId());
        int userId = request.getUserId();
        //特殊账户编码
        int specialId = ptCommonDao.getSpecialUserId();
        Map<String, SysFundAccountType> accountTypeMap = XinWangUtil.getWLZHMap();
        SysFundAccountType accountType;
        if (userId == specialId) {
            accountType = accountTypeMap.get(request.getPlatformUserNo());
        } else {
            UserRole userRole = null;
            try {
                userRole = UserRole.parse(request.getPlatformUserNo().replaceAll("[0-9]", ""));
            } catch (Exception e) {
                throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
            }
            accountType = accountTypeMap.get(userRole.getCode());
        }
        XWFundAccount fundAccount = userInfoService.getFundAccount(userId, accountType);

        if (fundAccount == null) {
            throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
        }
        //平台用户的资金账户入账
        List<XWCapitalFlow> flowList = new ArrayList<>();
        XWCapitalFlow flow = new XWCapitalFlow();
        {
            fundAccount.setAmount(fundAccount.getAmount().add(rechargeOrder.F03));
            flow.setFundAccountId(fundAccount.getId());
            flow.setTadeType(businessType.getCode());
            flow.setOtherFundAccountId(fundAccount.getId());
            flow.setIncome(rechargeOrder.F03);
            flow.setBalance(fundAccount.getAmount());
            flow.setRemark(businessType.getName());
            flow.setOrderId(orderId);
            Map<String, Object> params = new HashMap<>(2);
            params.put("id", fundAccount.getId());
            params.put("amount", rechargeOrder.F03);
            accountDao.updateFundAccountPlus(params);
        }
        //线下充值需要修改平台资金账户
        XWCapitalFlow ptFlow = new XWCapitalFlow();
        if (SysTradeFeeCode.CZ_XX == businessType.getCode() || SysTradeFeeCode.HBJE == businessType.getCode() || SysTradeFeeCode.CASH_VOUCHER == businessType.getCode()) {
            int ptUserId = ptCommonDao.getSpecialUserId();
            XWFundAccount ptFundAccount = userInfoService.getFundAccount(ptUserId, SysFundAccountType.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH);
            if (ptFundAccount == null) {
                logger.warn("平台资金账户不存在！");
                throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
            }
            if (ptFundAccount.getAmount().compareTo(rechargeOrder.F03) < 0) {
                logger.warn(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
                throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
            }
            //线下充值需要修改入账流水
            flow.setOtherFundAccountId(ptFundAccount.getId());
            ptFundAccount.setAmount(ptFundAccount.getAmount().subtract(rechargeOrder.F03));
            ptFlow.setFundAccountId(ptFundAccount.getId());
            ptFlow.setTadeType(businessType.getCode());
            ptFlow.setOtherFundAccountId(fundAccount.getId());
            ptFlow.setExpenditure(rechargeOrder.F03);
            ptFlow.setBalance(ptFundAccount.getAmount());
            ptFlow.setRemark(businessType.getName());
            ptFlow.setOrderId(orderId);
            Map<String, Object> params = new HashMap<>(2);
            params.put("id", ptFundAccount.getId());
            params.put("amount", rechargeOrder.F03);
            accountDao.updateFundAccountMinus(params);
            flowList.add(ptFlow);
        }
        flowList.add(flow);
        ptCommonDao.batchInsertT6102(flowList);
        //结束订单
        sysOrderService.success(orderId);
        xwRequest.setState(XWRequestState.CG);
        requestDao.updateRequest(xwRequest);
    }

    @Transactional
    public void rechargeSuccess(XWRequest xwRequest, SystemOrder order, BusinessType businessType) {
        XWRequest request = requestDao.getByRequestNo(xwRequest.getRequestNo());
        RechargeOrderEntity rechargeOrder = rechargeManageDao.getOrder(request.getOrderId());
        int userId = request.getUserId();
        if (rechargeOrder == null || order == null) {
            logger.warn("充值订单不存在，t6501.f01=[{}],userId=[{}]", request.getOrderId(), userId);
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        if (order.getOrderStatus().equals(XWOrderStatus.DQR)) {
            //特殊账户编码
            int specialId = ptCommonDao.getSpecialUserId();
            Map<String, SysFundAccountType> accountTypeMap = XinWangUtil.getWLZHMap();
            SysFundAccountType accountType;
            if (userId == specialId) {
                accountType = accountTypeMap.get(request.getPlatformUserNo());
            } else {
                UserRole userRole = null;
                try {
                    userRole = UserRole.parse(request.getPlatformUserNo().replaceAll("[0-9]", ""));
                } catch (Exception e) {
                    throw new XWTradeException(XWResponseCode.COMMON_PARAM_WRONG);
                }
                accountType = accountTypeMap.get(userRole.getCode());
            }
            XWFundAccount fundAccount = userInfoService.getFundAccount(userId, accountType);

            if (fundAccount == null) {
                throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
            }
            //平台用户的资金账户入账
            List<XWCapitalFlow> flowList = new ArrayList<>();
            XWCapitalFlow flow = new XWCapitalFlow();
            {
                fundAccount.setAmount(fundAccount.getAmount().add(rechargeOrder.F03));
                flow.setFundAccountId(fundAccount.getId());
                flow.setTadeType(businessType.getCode());
                flow.setOtherFundAccountId(fundAccount.getId());
                flow.setIncome(rechargeOrder.F03);
                flow.setBalance(fundAccount.getAmount());
                flow.setRemark(businessType.getName());
                flow.setOrderId(request.getOrderId());
                Map<String, Object> params = new HashMap<>(2);
                params.put("id", fundAccount.getId());
                params.put("amount", rechargeOrder.F03);
                accountDao.updateFundAccountPlus(params);
            }
            //线下充值需要修改平台资金账户
            XWCapitalFlow ptFlow = new XWCapitalFlow();
            if (SysTradeFeeCode.CZ_XX == businessType.getCode()) {
                int ptUserId = ptCommonDao.getSpecialUserId();
                XWFundAccount ptFundAccount = userInfoService.getFundAccount(ptUserId, SysFundAccountType.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH);
                if (ptFundAccount == null) {
                    logger.warn("平台资金账户不存在！");
                    throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
                }
                if (ptFundAccount.getAmount().compareTo(rechargeOrder.F03) < 0) {
                    logger.warn(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
                    throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
                }
                //线下充值需要修改入账流水
                flow.setOtherFundAccountId(ptFundAccount.getId());
                ptFundAccount.setAmount(ptFundAccount.getAmount().subtract(rechargeOrder.F03));
                ptFlow.setFundAccountId(ptFundAccount.getId());
                ptFlow.setTadeType(businessType.getCode());
                ptFlow.setOtherFundAccountId(fundAccount.getId());
                ptFlow.setExpenditure(rechargeOrder.F03);
                ptFlow.setBalance(ptFundAccount.getAmount());
                ptFlow.setRemark(businessType.getName());
                ptFlow.setOrderId(request.getOrderId());
                Map<String, Object> params = new HashMap<>(2);
                params.put("id", ptFundAccount.getId());
                params.put("amount", rechargeOrder.F03);
                accountDao.updateFundAccountMinus(params);
                flowList.add(ptFlow);
            }
            flowList.add(flow);
            ptCommonDao.batchInsertT6102(flowList);
            //结束订单
            sysOrderService.success(order.getId());
            xwRequest.setState(XWRequestState.CG);
            requestDao.updateRequest(xwRequest);
        }
    }

    /**
     * 处理划拨成功
     * @param sourceUserNo
     * @param targetUserNo
     * @param request
     * @param businessType
     * @param amount
     */
    @Transactional
    public void fundsTransferSuccess(String sourceUserNo, String targetUserNo, XWRequest request, BusinessType businessType, BigDecimal amount) {
        int userId = request.getUserId();
//        SystemOrder systemOrder = orderManageDao.get(request.getOrderId(),true);//lock up
        Map<String, SysFundAccountType> accountTypeMap = XinWangUtil.getWLZHMap();
        //插入划拨记录
        XWFundAccount czFundAccount = userInfoService.getFundAccount(userId, accountTypeMap.get(sourceUserNo));
        if (czFundAccount == null) {
            logger.warn("平台资金账户不存在！往来账户:[{}]", sourceUserNo);
            throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
        }
        if (czFundAccount.getAmount().compareTo(amount) < 0) {
            logger.warn(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
            throw new XWTradeException(XWResponseCode.PAYMENT_INSUFFICIENT_BALANCE);
        }
        czFundAccount.setAmount(czFundAccount.getAmount().subtract(amount));
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", czFundAccount.getId());
        params.put("amount", amount);
        accountDao.updateFundAccountMinus(params);
        XWFundAccount rzFundAccount = userInfoService.getFundAccount(userId, accountTypeMap.get(targetUserNo));
        if (rzFundAccount == null) {
            logger.warn("平台资金账户不存在！往来账户:[{}]", targetUserNo);
            throw new XWTradeException(XWResponseCode.FUND_ACCOUNT_NOT_EXIST);
        }
        rzFundAccount.setAmount(rzFundAccount.getAmount().add(amount));
        params.put("id", rzFundAccount.getId());
        params.put("amount", amount);
        accountDao.updateFundAccountPlus(params);
        //插入资金流水
        List<XWCapitalFlow> flowList = new ArrayList<>();
        XWCapitalFlow czFlow = new XWCapitalFlow();
        czFlow.setFundAccountId(czFundAccount.getId());
        czFlow.setTadeType(businessType.getCode());
        czFlow.setOtherFundAccountId(rzFundAccount.getId());
        czFlow.setExpenditure(amount);
        czFlow.setBalance(czFundAccount.getAmount());
        czFlow.setRemark(businessType.getName());
        czFlow.setOrderId(request.getOrderId());
        flowList.add(czFlow);
        XWCapitalFlow rzFlow = new XWCapitalFlow();
        rzFlow.setFundAccountId(rzFundAccount.getId());
        rzFlow.setTadeType(businessType.getCode());
        rzFlow.setOtherFundAccountId(czFundAccount.getId());
        rzFlow.setIncome(amount);
        rzFlow.setBalance(rzFundAccount.getAmount());
        rzFlow.setRemark(businessType.getName());
        rzFlow.setOrderId(request.getOrderId());
        flowList.add(rzFlow);
        ptCommonDao.batchInsertT6102(flowList);
        //插入新网划拨记录
        List<XWFundsTransfer> fundsTransferList = new ArrayList<>();
        XWFundsTransfer czTransfer = new XWFundsTransfer();
        czTransfer.setRequestNo(request.getRequestNo());
        czTransfer.setPlatformUserNo(sourceUserNo);
        czTransfer.setOtherUserNo(targetUserNo);
        czTransfer.setIncome(BigDecimal.ZERO);
        czTransfer.setOutcome(amount);
        fundsTransferList.add(czTransfer);
        XWFundsTransfer rzTransfer = new XWFundsTransfer();
        rzTransfer.setRequestNo(request.getRequestNo());
        rzTransfer.setPlatformUserNo(targetUserNo);
        rzTransfer.setOtherUserNo(sourceUserNo);
        rzTransfer.setIncome(amount);
        rzTransfer.setOutcome(BigDecimal.ZERO);
        fundsTransferList.add(rzTransfer);
        fundsTransferService.batchInsertFlow(fundsTransferList);
        //结束订单
        sysOrderService.success(request.getOrderId());
        request.setState(XWRequestState.CG);
        requestDao.updateRequest(request);
    }

}
