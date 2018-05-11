package com.fenlibao.service.pms.da.biz.investplan.impl;


import com.fenlibao.common.pms.util.ResponseCode;
import com.fenlibao.dao.pms.da.biz.investplan.PlanBidMapper;
import com.fenlibao.model.pms.da.biz.plan.SysOrderVO;
import com.fenlibao.model.pms.da.biz.plan.TradeRecord;
import com.fenlibao.model.pms.da.biz.plan.UserAccount;
import com.fenlibao.model.pms.da.global.FeeCode;
import com.fenlibao.model.pms.da.global.InterfaceConst;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.service.pms.da.biz.investplan.PlanOrderService;
import com.fenlibao.service.pms.da.exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
@Service
public class PlanOrderServiceImpl implements PlanOrderService {

    private static final Logger LOGGER = LogManager.getLogger(PlanOrderServiceImpl.class);

    @Resource
    private PlanBidMapper planBidMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addErrorLog(int orderId, Throwable exception) throws Exception {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Charset charset = Charset.forName("UTF-8");
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
                    out, charset));
            exception.printStackTrace(printWriter);
            printWriter.flush();
            Map<String, String> params = new HashMap<>();
            params.put("orderId", "" + orderId);
            params.put("exceptionStr", new String(out.toByteArray(), charset));
            planBidMapper.addErrorLog(params);
        } catch (Exception e) {
            LOGGER.error("保存计划订单异常日志时发生异常：", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Integer createInvestPlanOrder(Integer accountId, Integer planId, BigDecimal investAmount) {
        // 插入投标订单
        SysOrderVO sysOrderVO = new SysOrderVO();
        sysOrderVO.setLevel("YH");
        sysOrderVO.setOrderType(FeeCode.BIDORDERTYPE);
        sysOrderVO.setUserId(accountId);
        sysOrderVO.setStatus("DTJ");
        planBidMapper.addSysOrder(sysOrderVO);
        Integer ordId = (sysOrderVO.getOrderId() == null ? 0 : sysOrderVO.getOrderId());
        planBidMapper.addPlanOrder(ordId, accountId, planId, investAmount);
        return ordId;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void submitKernel(int orderId) {
        SysOrderVO sysOrderVO = planBidMapper.lockOrderById(orderId);
        if (sysOrderVO != null && "DTJ".equals(sysOrderVO.getStatus())) {
            updatePlanOrder("DQR", orderId);
        }
    }

    @Override
    public void confirmKernel(int orderId, Map<String, String> rtnMap) {
      // code here to confirm order ......
        SysOrderVO sysOrderVO = planBidMapper.lockOrderById(orderId);
        if (sysOrderVO != null && "DQR".equals(sysOrderVO.getStatus())) {
            updatePlanOrder("CG", orderId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updatePlanOrder(String sb, Integer orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("status", sb);
        planBidMapper.updatePlanOrder(params);
    }

    @Override
    public void returnBackUserPlanId(int userPlanId, Integer orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("userPlanId", userPlanId);
        planBidMapper.returnBackUserPlanId(params);
    }

    @Override
    public void lockUserAmountForPlan(int orderId, String number, String title, int userId, BigDecimal amount) {

        SysFundAccountType wlzh = SysFundAccountType.XW_INVESTOR_WLZH;
        SysFundAccountType sdzh = SysFundAccountType.XW_INVESTOR_SDZH;
        UserAccount userAccount = getUserAccountBy(userId, wlzh.name());
        // 锁定投资人资金账户
        UserAccount lockWlzhAccount = planBidMapper.lockUserAccountById(userAccount.getAccountId());
        userAccount = getUserAccountBy(userId, sdzh.name());
        // 锁定入账账户
        UserAccount lockSdzhAccount = planBidMapper.lockUserAccountById(userAccount.getAccountId());
        int id = planBidMapper.getBidTypeByCode("JH");
        int feeCode = Integer.valueOf("" + id + InterfaceConst.TZ);
        {
            // 扣减出账账户金额
            lockWlzhAccount.setBalance(lockWlzhAccount.getBalance().subtract(amount));
            if (lockWlzhAccount.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT);    //账户金额不足
            }
            planBidMapper.updateUserAmount(lockWlzhAccount.getBalance(), lockWlzhAccount.getAccountId());
            // 资金流水
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setPayAccountId(lockWlzhAccount.getAccountId());
            tradeRecord.setPayTypeId(feeCode);
            tradeRecord.setIncomeAccountId(lockSdzhAccount.getAccountId());
            tradeRecord.setPayAmount(amount);
            tradeRecord.setIncomeAmount(BigDecimal.ZERO);
            tradeRecord.setBalance(lockWlzhAccount.getBalance());
            tradeRecord.setDescription(String.format("计划投资:%s，标题：%s", number, title));
            tradeRecord.setOrderId(orderId);
            planBidMapper.addTradeRecord(tradeRecord);
        }
        {
            // 增加入账账户金额
            lockSdzhAccount.setBalance(lockSdzhAccount.getBalance().add(amount));
            planBidMapper.updateUserAmount(lockSdzhAccount.getBalance(), lockSdzhAccount.getAccountId());
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setPayAccountId(lockSdzhAccount.getAccountId());
            tradeRecord.setPayTypeId(feeCode);
            tradeRecord.setIncomeAccountId(lockWlzhAccount.getAccountId());
            tradeRecord.setPayAmount(BigDecimal.ZERO);
            tradeRecord.setIncomeAmount(amount);
            tradeRecord.setBalance(lockSdzhAccount.getBalance());
            tradeRecord.setDescription(String.format("计划投资:%s，标题：%s", number, title));
            tradeRecord.setOrderId(orderId);
            planBidMapper.addTradeRecord(tradeRecord);
        }
    }

    private UserAccount getUserAccountBy(int userId, String name) {
        UserAccount userAccount = planBidMapper.getUserAccount(userId, name);
        if (userAccount == null) {
            LOGGER.error("投资人新网账户：[{}]不存在,userId=[{}]",name, userId);
            if (name.equals(SysFundAccountType.XW_INVESTOR_WLZH.name())) {
                throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);    //投资人往来账户不存在
            }
            if (name.equals(SysFundAccountType.XW_INVESTOR_SDZH.name())) {
                throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);    //投资人锁定账户不存在
            }
        }
        return userAccount;
    }
}
