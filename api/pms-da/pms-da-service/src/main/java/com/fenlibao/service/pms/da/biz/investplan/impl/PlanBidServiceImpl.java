package com.fenlibao.service.pms.da.biz.investplan.impl;

import com.fenlibao.common.pms.util.ResponseCode;
import com.fenlibao.common.pms.util.loader.Config;
import com.fenlibao.dao.pms.da.biz.investplan.PlanBidMapper;
import com.fenlibao.model.pms.da.biz.plan.BidForPlanVO;
import com.fenlibao.model.pms.da.biz.plan.InvestPlanVO;
import com.fenlibao.model.pms.da.biz.plan.InvestUserPlan;
import com.fenlibao.model.pms.da.global.SpecialUserType;
import com.fenlibao.service.pms.common.message.sms.SmsService;
import com.fenlibao.service.pms.da.biz.investplan.*;
import com.fenlibao.service.pms.da.exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
@Service
public class PlanBidServiceImpl implements PlanBidService {

    private static final Logger LOGGER = LogManager.getLogger(PlanBidServiceImpl.class);

    @Autowired
    private SpecialUserService specialUserService;
    @Autowired
    private PlanBidMatchBidService planBidMatchBidService;
    @Autowired
    private PlanOrderService planOrderService;
    @Autowired
    private InvestPlanService investPlanService;
    @Autowired
    private SmsService smsService;


    @Autowired
    private PlanBidMapper planBidMapper;


    /**
     * 购买计划
     * @param accountId
     * @param planId
     * @param investPlan
     * @param amount
     * @param bidForPlanVOs
     * @throws Exception
     */
    private Integer investmentPlan(Integer accountId, Integer planId, InvestPlanVO investPlan, BigDecimal amount, List<BidForPlanVO> bidForPlanVOs) throws Exception {

        //检查计划可投资余额，是否满足
        if (investPlan.getSurplusAmount().compareTo(amount) < 0) {
            LOGGER.info("投资金额[{}]已超过剩余可投金额[{}], planId：{}",amount, investPlan.getSurplusAmount(), planId);
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //投资金额已超过剩余可投金额
        }
        Integer orderId = 0;
        try {
            // 创建购买计划订单
            orderId = planOrderService.createInvestPlanOrder(accountId, planId, amount);
            // 提交订单
            planOrderService.submitKernel(orderId);
        } catch (Exception e) {
            if (orderId != 0) {
                planOrderService.updatePlanOrder("SB", orderId);
                planOrderService.addErrorLog(orderId, e);
            }
            LOGGER.error("购买计划时创建计划订单/提交订单时异常：", e);
            throw e;
        }
        int userPlanId = 0;
        try {
            // 扣减计划可投金额
            updateSurplusAmountForInvestPlan(investPlan, amount, accountId);
            // 增加计划投资记录
            userPlanId = insertRecordForInvestPlan(accountId, investPlan.getId(), amount, amount);
            // 冻结用户存管账户金额
            planOrderService.lockUserAmountForPlan(orderId, investPlan.getNumber(), investPlan.getTitle(), accountId, amount);

            BigDecimal surplusAmount = planBidMatchBidService.investBidForPlan(planId, userPlanId, accountId, amount, bidForPlanVOs);
            if (surplusAmount.compareTo(BigDecimal.ZERO) != 0) {
                LOGGER.error("计划配标时还有可投金额：{}，不能全部匹配到标，异常", surplusAmount);
                throw new BusinessException(ResponseCode.BID_INVESTMENT_FAILURE);
            }
            planOrderService.returnBackUserPlanId(userPlanId, orderId);
            // 确认订单
            planOrderService.confirmKernel(orderId, null);
            LOGGER.info("用户投资计划成功.......");
        } catch (Exception e) {
            planOrderService.updatePlanOrder("SB", orderId);
            planOrderService.addErrorLog(orderId, e);
            throw e;
        }
        //新网不冻结金额，只需要保证本地账户资金被冻结就可
        //xwFundService.doFreezeFund(accountId, amount);
        return userPlanId;
    }

    // 因为循环处理每个需要封的计划，所以开启一个新的事务，这样如果某个封计划异常就不会回滚之前成功的计划
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void terminationPlan(Integer planId, int pmsUserId) throws Exception {
        InvestPlanVO investPlan = planBidMapper.lockInvestPlanById(planId);
        if (investPlan.getStatus() != 4) {
            LOGGER.error("投资计划的状态不为投资中,不能投资.......");
            throw new BusinessException(ResponseCode.BID_FULLED); // 该项目已满额，请投资其他项目
        }
        BigDecimal investAmount = BigDecimal.ZERO; // 需要购买的金额
        List<String> userIds = specialUserService.getUserIds(SpecialUserType.INVEST_PLAN_NOT_FULL);
        if (userIds == null || userIds.size() == 0) {
            LOGGER.warn("(封计划)投资计划时没有可以使用账号..........");
            throw new Exception("(封计划)投资计划时没有可以使用账号");
        }
        // 封计划前先处理需要处理计划绑定没有满额的标
        List<BidForPlanVO> bidForPlanVOs = planBidMapper.getPlanBindBids(planId, false);
        for (BidForPlanVO bidForPlanVO : bidForPlanVOs) {
            investAmount = investAmount.add(bidForPlanVO.getVoteAmount());
        }
        if (investAmount.compareTo(BigDecimal.ZERO) > 0) {
            // 购买计划的人
            int userId = Integer.parseInt(userIds.get(0));
            try {
                // 购买计划
                investmentPlan(userId, planId, investPlan, investAmount, bidForPlanVOs);
            } catch (Exception e) {
                if (ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT.getCode()
                        .equals(((BusinessException) e).getCode())) {
                    //删掉缓存中的userId
                    specialUserService.lremSpecialUser(String.valueOf(userId), SpecialUserType.INVEST_PLAN_NOT_FULL);
                    //短信通知
                    String smsPattern = String.format("(封计划时)账户余额不足，需投资金额：%s，,请及时处理(userId:%s)", investAmount, userId);
                    LOGGER.warn(smsPattern);
                    String phone = Config.get("closure.plan.account.notify");
                    smsService.sendMsg(phone, smsPattern);
                }
                throw e;
            }
        }
        // 释放计划绑定的标 code here ......
        planBidMapper.unBindPlanBids(planId);
        // 封计划
        investPlanService.termination(planId, pmsUserId);
    }

    /**
     * 扣减计划可投金额
     *
     * @param investPlan
     * @param amount
     * @param accountId
     * @return
     */
    private int updateSurplusAmountForInvestPlan(InvestPlanVO investPlan, BigDecimal amount, int accountId) {
        int res = planBidMapper.updateSurplusAmountForInvestPlan(investPlan.getId(), amount);
        if (res == 0) {
            LOGGER.error("扣减计划可投金额失败,userId=[{}],investPlanId=[{}]", accountId, investPlan.getId());
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH);    //扣减计划可投金额失败
        }
        return res;
    }

    /**
     * 添加计划投资记录
     *
     * @param userId
     * @param planId
     * @param amount
     * @return
     */
    private int insertRecordForInvestPlan(int userId, int planId, BigDecimal amount, BigDecimal freezeAmount) {
        InvestUserPlan investUserPlan = new InvestUserPlan();
        investUserPlan.setUserId(userId);
        investUserPlan.setPlanId(planId);
        investUserPlan.setInvestAmount(amount);
        investUserPlan.setFreezeAmount(freezeAmount);
        planBidMapper.insertRecordForInvestPlan(investUserPlan);
        if (investUserPlan.getId() == 0) {
            LOGGER.error("添加计划投资记录失败,userId=[{}],investPlanId=[{}]", userId, planId);
            throw new BusinessException(ResponseCode.BID_INVESTMENT_FAILURE);    //添加计划投资记录失败
        }
        return investUserPlan.getId();
    }

}
