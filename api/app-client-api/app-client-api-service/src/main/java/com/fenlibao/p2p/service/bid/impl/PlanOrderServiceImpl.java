package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.UserAccountDao;
import com.fenlibao.p2p.dao.plan.PlanBidDao;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.entity.plan.PlanBid;
import com.fenlibao.p2p.model.entity.plan.TradeRecord;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.FeeCode;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.plan.SysOrderVO;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.service.bid.PlanOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanOrderServiceImpl.class);

    @Resource
    private PlanBidDao planBidDao;
    @Resource
    private UserAccountDao userAccountDao;

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
            planBidDao.addErrorLog(params);
        } catch (Exception e) {
            LOGGER.error("保存计划订单异常日志时发生异常：", e);
        }
    }

    @Transactional
    @Override
    public Map<String, String> createInvestBidOrder(int accountId, PlanBid planBid, BigDecimal amount) {
        if (planBid == null) {
            throw new BusinessException(ResponseCode.BID_NOT_EXIST);     //指定的标记录不存在
        }

        BigDecimal _a = planBid.getVoteAmount().stripTrailingZeros();
        if (_a.toPlainString().contains(".")) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_INTEGER); //投标金额必须为整数
        }
        Map<String, String> rtnMap = new HashMap<String, String>();
        boolean zjb = "true".equals(InterfaceConst.BID_SFZJKT); //PS:这里应该是动态获取资源
        if (zjb && accountId == planBid.getUserId()) {
            throw new BusinessException(ResponseCode.BID_BORROWER_CANNOT_INVESTMENT);     //您是该标的借款人，不能投标
        }
        if (!"TBZ".equals(planBid.getStatus())) {
            LOGGER.error("计划配标时[{}]标不是投标中状态[{}],不能投标,accountId=[{}]", planBid.getBidId(), planBid.getStatus(), accountId);
            throw new BusinessException(ResponseCode.BID_FULLED);     //指定的标不是投标中状态,不能投标  //160617按产品要求提示 该项目已满标，请出借其他项目
        }
        if (planBid.getVoteAmount().compareTo(new BigDecimal(0)) <= 0 ) {
            LOGGER.error("计划配标时标的可出借额为：{}", planBid.getVoteAmount());
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH);
        }
        BigDecimal investAmount = planBid.getVoteAmount().compareTo(amount) <= 0 ? planBid.getVoteAmount() : amount;
        Integer ordId = 0;
        // 插入投标订单
        ordId = planBidDao.addSysOrder(FeeCode.BIDORDERTYPE, "DTJ", "YH", accountId);
        planBidDao.addBidOrder(ordId, accountId, planBid.getBidId(), investAmount);
        rtnMap.put("orderId", String.valueOf(ordId));
        return rtnMap;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Integer createInvestPlanOrder(Integer accountId, Integer planId, BigDecimal investAmount) {
        Integer ordId = 0;
        // 插入投标订单
        ordId = planBidDao.addSysOrder(FeeCode.BIDORDERTYPE, "DTJ", "YH", accountId);
        planBidDao.addPlanOrder(ordId, accountId, planId, investAmount);
        return ordId;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void submitKernel(int orderId) {
        SysOrderVO sysOrderVO = planBidDao.lockOrderById(orderId);
        if (sysOrderVO != null && "DTJ".equals(sysOrderVO.getStatus())) {
            updatePlanOrder("DQR", orderId);
        }
    }

    @Override
    public void confirmKernel(int orderId, Map<String, String> rtnMap) {
      // code here to confirm order ......
        SysOrderVO sysOrderVO = planBidDao.lockOrderById(orderId);
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
        planBidDao.updatePlanOrder(params);
    }

    @Override
    public void returnBackUserPlanId(int userPlanId, Integer orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("userPlanId", userPlanId);
        planBidDao.returnBackUserPlanId(params);
    }

    @Override
    public void lockUserAmountForPlan(int orderId, String number, String title, int userId, BigDecimal amount) {

        SysFundAccountType wlzh = SysFundAccountType.XW_INVESTOR_WLZH;
        SysFundAccountType sdzh = SysFundAccountType.XW_INVESTOR_SDZH;
        UserAccount userAccount = getUserAccountBy(userId, wlzh.name());
        // 锁定出借人资金账户
        UserAccount lockWlzhAccount = planBidDao.lockUserAccountById(userAccount.getAccountId());
        userAccount = getUserAccountBy(userId, sdzh.name());
        // 锁定入账账户
        UserAccount lockSdzhAccount = planBidDao.lockUserAccountById(userAccount.getAccountId());
        int id = planBidDao.getBidTypeByCode("JH");
        int feeCode = Integer.valueOf("" + id + InterfaceConst.TZ);
        {
            // 扣减出账账户金额
            lockWlzhAccount.setBalance(lockWlzhAccount.getBalance().subtract(amount));
            if (lockWlzhAccount.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT);    //账户金额不足
            }
            planBidDao.updateUserAmount(lockWlzhAccount.getBalance(), lockWlzhAccount.getAccountId());
            // 资金流水
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setPayAccountId(lockWlzhAccount.getAccountId());
            tradeRecord.setPayTypeId(feeCode);
            tradeRecord.setIncomeAccountId(lockSdzhAccount.getAccountId());
            tradeRecord.setPayAmount(amount);
            tradeRecord.setIncomeAmount(BigDecimal.ZERO);
            tradeRecord.setBalance(lockWlzhAccount.getBalance());
            tradeRecord.setDescription(String.format("计划出借:%s，标题：%s", number, title));
            tradeRecord.setOrderId(orderId);
            planBidDao.addTradeRecord(tradeRecord);
        }
        {
            // 增加入账账户金额
            lockSdzhAccount.setBalance(lockSdzhAccount.getBalance().add(amount));
            planBidDao.updateUserAmount(lockSdzhAccount.getBalance(), lockSdzhAccount.getAccountId());
            TradeRecord tradeRecord = new TradeRecord();
            tradeRecord.setPayAccountId(lockSdzhAccount.getAccountId());
            tradeRecord.setPayTypeId(feeCode);
            tradeRecord.setIncomeAccountId(lockWlzhAccount.getAccountId());
            tradeRecord.setPayAmount(BigDecimal.ZERO);
            tradeRecord.setIncomeAmount(amount);
            tradeRecord.setBalance(lockSdzhAccount.getBalance());
            tradeRecord.setDescription(String.format("计划出借:%s，标题：%s", number, title));
            tradeRecord.setOrderId(orderId);
            planBidDao.addTradeRecord(tradeRecord);
        }
    }

    private UserAccount getUserAccountBy(int userId, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "" + userId);
        params.put("type", name);
        UserAccount userAccount = userAccountDao.getUserAccount(params);
        if (userAccount == null) {
            LOGGER.error("出借人新网账户：[{}]不存在,userId=[{}]",name, userId);
            if (name.equals(SysFundAccountType.XW_INVESTOR_WLZH.name())) {
                throw new BusinessException(ResponseCode.USER_WLZH_ACCOUNT_NOT_EXIST);    //出借人往来账户不存在
            }
            if (name.equals(SysFundAccountType.XW_INVESTOR_SDZH.name())) {
                throw new BusinessException(ResponseCode.USER_SDZH_ACCOUNT_NOT_EXIST);    //出借人锁定账户不存在
            }
        }
        return userAccount;
    }
}
