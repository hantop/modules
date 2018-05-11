package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.dao.plan.PlanBidDao;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.plan.BidForPlanVO;

import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.PlanBidMatchBidService;
import com.fenlibao.p2p.service.bid.PlanBidService;
import com.fenlibao.p2p.service.bid.PlanExtService;
import com.fenlibao.p2p.service.bid.PlanOrderService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
@Service
public class PlanBidServiceImpl implements PlanBidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanBidServiceImpl.class);

    @Resource
    private PlanBidHandler planBidHandler;
    @Resource
    private PlanBidDao planBidDao;
    @Resource
    private PlanBidMatchBidService planBidMatchBidService;
    @Resource
    private PlanOrderService planOrderService;

    @Resource
    private PlanDao planDao;
    @Resource
    private ITradeDao tradeDao;
    @Resource
    private PlanExtService planExtService;
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private XWUserInfoService xwUserInfoService;

    /**
     * 购买计划
     * @param accountId
     * @param planId
     * @param investPlan
     * @param amount
     * @param bidForPlanVOs
     * @throws Exception
     */
    private Map<String, Integer> investmentPlan(Integer accountId, Integer planId, InvestPlan investPlan, BigDecimal amount, List<BidForPlanVO> bidForPlanVOs) throws Exception {

        //检查计划可投资余额，是否满足
        if (investPlan.getSurplusAmount().compareTo(amount) < 0) {
            LOGGER.info("投资金额[{}]已超过剩余可出借额[{}], planId：{}",amount, investPlan.getSurplusAmount(), planId);
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //投资金额已超过剩余可投金额
        }
        Map<String, Integer> resultMap = new HashMap<>();
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
                LOGGER.error("计划配标时还有可出借额：{}，不能全部匹配到标，异常", surplusAmount);
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
        resultMap.put("orderId", orderId);
        resultMap.put("userPlanId", userPlanId);
        return resultMap;
    }

    @Transactional
    @Override
    public boolean doInvestPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId) throws Exception {

        //限制每个用户同一次投标频率为1分钟
        int lastOrderTime = userInfoService.getUserInvestPlanLastOrder(accountId, planId);
        if (lastOrderTime != 0 && lastOrderTime < 60) {
            throw new LogicalException(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode()); //投标过于频繁
        }

        InvestPlan investPlan = tradeDao.getInvestPlan(planId, 1);
        boolean isSuccess = true;
        if (investPlan.getStatus() != 4) {
            LOGGER.error("投资计划的状态不为投资中,不能投资.......");
            throw new BusinessException(ResponseCode.BID_FULLED); // 该项目已满额，请投资其他项目
        }
        //投标金额必须为整数 kris
        BigDecimal _a = amount.stripTrailingZeros();
        if (_a.toPlainString().contains(".")) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_INTEGER); //投标金额必须为整数
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0 ) {
            LOGGER.error("购买的金额为负数：{}", amount);
            throw  new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_GT0);
        }
        BigDecimal min = new BigDecimal(InterfaceConst.BID_MIX_AMOUNT);

        if (amount.compareTo(min) < 0) {
            LOGGER.error("投计划金额不能低于最低出借额 planId：{}, amount:{}, minAmount:{}", planId, amount, min);
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_LT100); // 投标金额不能低于最低起投金额
        }
        // 查询是否有逾期未还
        int count = planBidDao.getYqCount(accountId);
        if (count > 0) {
            LOGGER.error("投计划时，存在未还记录，不能投资userId: {}", accountId);
            throw new BusinessException(ResponseCode.BIT_LOAN_OVERDUE); //借款逾期未还，不能投标！
        }
        // 当该计划中绑定该用户的借款标时，改用户不能投资该计划
        BidForPlanVO bidForPlanVO = planBidDao.thisUserBorrowBidInPlan(planId, accountId);
        if (bidForPlanVO != null) {
            LOGGER.error("用户：{}投资投计划:{}时，其借款标在该计划中，不能投资.....", accountId, planId);
            throw new BusinessException(ResponseCode.BID_BORROWER_CANNOT_INVESTMENT);
        }
        Timestamp nowDate = tradeDao.getDBCurrentTime();
        // 验证返现券是否符合条件
        List<UserRedPacketInfo> userRedPacketInfos = planBidHandler.checkRedPackets(investPlan, fxhbIds, amount, nowDate);
        // 验证加息券是否符合条件
        planBidHandler.checkCoupon(investPlan, investPlan.getCycleType(), investPlan.getCycle(), jxqId, accountId, amount, nowDate);
        // 获取计划绑定且投资中状态的标
        List<BidForPlanVO> bidForPlanVOs = planBidDao.getPlanBindBids(planId, true);
        // 购买计划
        Map<String, Integer> resultMap = investmentPlan(accountId, planId, investPlan, amount, bidForPlanVOs);

        //获取用户信息
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", accountId);
        UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(userMap);
        //使用返现券
        if (!StringUtils.isBlank(fxhbIds)) {
            try{
                planExtService.useRedPackets(resultMap.get("orderId"), userRedPacketInfos, investPlan.getId(), resultMap.get("userPlanId"), userInfo, fxhbIds, InterfaceConst.FXHB_INVEST_TYPE_INVESTPLAN, VersionTypeEnum.CG);
            }catch (Exception e){
                LOGGER.error(String.format("计划使用红包 %s", e));
            }
        }
        //使用加息券
        if (!StringUtils.isBlank(jxqId)) {
            try{
                planExtService.useCoupon(resultMap.get("userPlanId"), userInfo, jxqId, InterfaceConst.FXHB_INVEST_TYPE_INVESTPLAN);
            }catch (Exception e){
                LOGGER.error(String.format("使用加息券 %s", e));
            }
        }
        try {
            String content = Sender.get("sms.dobid.content").replace("#{bidTitle}", investPlan.getTitle()).replace("#{investSum}", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            planExtService.sendSmsAndLetter(userInfo.getPhone(), String.valueOf(accountId), content, VersionTypeEnum.CG);
        } catch (Throwable t) {
            LOGGER.error("投资计划后短信和站内信异常", t);
        }
        try {
            if(investPlan.getSurplusAmount().subtract(amount).compareTo(BigDecimal.ZERO) == 0){
                planExtService.updateInvestPlanStatus(investPlan.getId());
            }
        } catch (Exception e) {
            LOGGER.error("检查封计划,更新状态异常", e);
        }
        return isSuccess;
    }

    /**
     * 扣减计划可投金额
     *
     * @param investPlan
     * @param amount
     * @param accountId
     * @return
     */
    private int updateSurplusAmountForInvestPlan(InvestPlan investPlan, BigDecimal amount, int accountId) {
        int res = planDao.updateSurplusAmountForInvestPlan(investPlan.getId(), amount);
        if (res == 0) {
            LOGGER.error("扣减计划可出借额失败,userId=[{}],investPlanId=[{}]", accountId, investPlan.getId());
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
        int id = planDao.insertRecordForInvestPlan(userId, planId, amount,freezeAmount);
        if (id == 0) {
            LOGGER.error("添加计划投资记录失败,userId=[{}],investPlanId=[{}]", userId, planId);
            throw new BusinessException(ResponseCode.BID_INVESTMENT_FAILURE);    //添加计划投资记录失败
        }
        return id;
    }

}
