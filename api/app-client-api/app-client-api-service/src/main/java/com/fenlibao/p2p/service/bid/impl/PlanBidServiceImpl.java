package com.fenlibao.p2p.service.bid.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.dao.plan.PlanBidDao;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.consts.plan.PlanBidConst;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.plan.PlanType;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.bid.PlanBidVO;
import com.fenlibao.p2p.model.vo.plan.BidForPlanVO;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.activity.ActivityService;
import com.fenlibao.p2p.service.bid.*;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.loader.Sender;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author zeronx on 2017/11/15 11:20.
 * @version 1.0
 */
@Service
public class PlanBidServiceImpl implements PlanBidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanBidServiceImpl.class);

    @Resource
    private SpecialUserService specialUserService;
    @Resource
    private PlanValidateHandler validateHandler;
    @Resource
    private PlanBidHandler planBidHandler;
    @Resource
    private PlanBidDao planBidDao;
    @Resource
    private RedisService redisService;
    @Resource
    private PlanBidMatchBidService planBidMatchBidService;
    @Resource
    private PlanOrderService planOrderService;
    @Resource
    private PlanService planService;

    @Resource
    private PlanDao planDao;
    @Resource
    private ITradeDao tradeDao;
    @Resource
    private PlanExtService planExtService;
    @Resource
    private UserInfoService userInfoService;

    @Override
    public boolean enabledReleasePlan() {
        Map<String, String> resultMap = planBidDao.getEnableReleasePlanConfig(PlanBidConst.ENABLED_AUTO_RELEASE_PLAN);
        if (resultMap != null) {
            return PlanBidConst.ENABLED_AUTO_RELEASE_PLAN_ON.equals(resultMap.get("is_enabled"));
        }
        return false;
    }

    @Override
    public void preparedReleasePlan() throws Exception {
        // 获取启用的计划模板
        List<PlanType> planTypes = planBidDao.getPlanTemplates();
        if (CollectionUtils.isEmpty(planTypes)) {
            LOGGER.warn("没有可以使用的计划模板......");
            return;
        }
        // 因为优先发布新手计划，由于种种原因，产品说 在数据库设置的优先级不能用，shit 表达了我一身愤腔
        List<PlanType> tempTypes = dealWithPriority(planTypes);
        // 获取这个时间点最新的且启用的利息管理费比列
        BigDecimal rateManageRatio = planBidDao.getRateManageRatio();
        if (rateManageRatio == null) {
            rateManageRatio = BigDecimal.ZERO;
        }
        // 遍历需要发布的模板：譬如：新手/省心计划 10天，20天，30天，
        for (PlanType planType : tempTypes) {
            planType.setRateManageRatio(rateManageRatio);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(PlanBidConst.PLAN_LOCK_KEY)
                        .append(planType.getCycle());
            try {
                // 防止分布式问题
                boolean lock = redisService.acquireLock(stringBuffer.toString(), 1000 * 60 * 4);
                if (!lock) {
                    continue;
                }
                // 统计状态为投标中的标的总金额是否足够发布计划
                boolean passed = checkBidSumAmountCanReleasePlan(planType);
                if (passed) {
                    // 新手计划
                    if (PlanBidConst.IS_NEWBIE_PLAN.equals(planType.getIsNoviceBid())) {
                        // 因为新手计划10天，20天，30天，只能在前端展示一条，即在数据库中只能有一条计划为投标中
                        Integer count = planBidDao.getCountNewPlanBy(planType);
                        if (count < PlanBidConst.DISPLAY_NEWBIE_COUNTS) {
                            releasePlan(planType, PlanBidConst.DISPLAY_NEWBIE_COUNTS - count);
                        }
                    } else { // 省心计划
                        releasePlan(planType, -1);
                    }
                }
            } catch (Exception e) {
                throw e;
            } finally {
                redisService.releaseLock(stringBuffer.toString());
            }
        }
    }

    /**
     * 处理优先级
     * @param planTypes
     * @return
     * 哎 只能用 是否是新手计划的标志来排序了
     */
    private List<PlanType> dealWithPriority(List<PlanType> planTypes) {
        Collections.sort(planTypes, new Comparator<PlanType>() {
            public int compare(PlanType arg0, PlanType arg1) {
                return arg1.getIsNoviceBid().compareTo(arg0.getIsNoviceBid());
            }
        });
        return planTypes;
    }

    /**
     * 发布计划
     * @param planType
     * @param num 最大能发布的记录数，-1：无限制
     */
    private void releasePlan(PlanType planType, int num) {

        Integer releaseNum = (num == -1 ? Integer.MAX_VALUE : num); // 前端最大展示数量
        List<BidForPlanVO> tempBindBids = new ArrayList<>();
        Map<String, Object> planMaps = new HashMap<>(); // 发布的每一条计划绑定的标
        BigDecimal sumAmount = BigDecimal.ZERO;
        int index = 0; // 表示当前的标的总金额可以发布多少条计划，也可以作为下标
        Map<String, Object> params = new HashMap<>();
        params.put("cycle", planType.getCycle());
        params.put("cycleType", planType.getCycleType());
        params.put("repaymentType", planType.getRepaymentType());
        List<BidForPlanVO> bidForPlanVOs = planBidDao.getBidsForPlanBy(params);
        for (BidForPlanVO bidForPlanVO : bidForPlanVOs) {

            sumAmount = sumAmount.add(bidForPlanVO.getVoteAmount());
            tempBindBids.add(bidForPlanVO);
            if (sumAmount.compareTo(planType.getAmount()) >= 0) {
                planMaps.put("plan_sum_amount_" + index, sumAmount);
                planMaps.put("plan_bid_list_" + index, tempBindBids);
                sumAmount = BigDecimal.ZERO;
                tempBindBids = new ArrayList<>();
                index ++;
                if (index >= releaseNum) {
                    break;
                }
            }
        }

        // 插入到数据库
        for (int i = 0; i < index; i++) {
            sumAmount = (BigDecimal)planMaps.get("plan_sum_amount_" + i);
            tempBindBids = (List<BidForPlanVO>)planMaps.get("plan_bid_list_" + i);
            try {
                // 每发布一个计划开启一个事务
                planBidHandler.releasePlan(planType, sumAmount, tempBindBids);
            } catch (Exception e) {
                LOGGER.error("发布计划【{}-{}天】异常：{}", planType.getTitle(), planType.getCycle(), e);
            }
        }
    }

    private boolean checkBidSumAmountCanReleasePlan(PlanType planType) {
        Map<String, Object> params = new HashMap<>();
        params.put("cycle", planType.getCycle());
        params.put("cycleType", planType.getCycleType());
        params.put("repaymentType", planType.getRepaymentType());
        BigDecimal sumAmount = planBidDao.getBidSumAmountBy(params);
        if (sumAmount.compareTo(planType.getAmount()) >= 0) {
            return true;
        }
        return false;
    }

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

        Map<String, Integer> resultMap = new HashMap<>();
        //检查计划可出借余额，是否满足
        if (investPlan.getSurplusAmount().compareTo(amount) < 0) {
            LOGGER.info("出借金额[{}]已超过剩余可出借额[{}], planId：{}",amount, investPlan.getSurplusAmount(), planId);
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //出借金额已超过剩余可投金额
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
            // 增加计划出借记录
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
            LOGGER.info("用户出借计划成功.......");
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

    // 因为循环处理每个需要封的计划，所以开启一个新的事务，这样如果某个封计划异常就不会回滚之前成功的计划
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void terminationPlan(Integer planId, int pmsUserId) throws Exception {
        InvestPlan investPlan = tradeDao.getInvestPlan(planId, 1);
        if (investPlan.getStatus() != 4) {
            LOGGER.error("出借计划的状态不为出借中,不能出借.......");
            throw new BusinessException(ResponseCode.BID_FULLED); // 该项目已满额，请出借其他项目
        }
        BigDecimal investAmount = BigDecimal.ZERO; // 需要购买的金额
        List<String> userIds = specialUserService.getUserIds(SpecialUserType.INVEST_PLAN_NOT_FULL);
        if (userIds == null || userIds.size() == 0) {
            LOGGER.warn("(封计划)出借计划时没有可以使用账号..........");
            throw new Exception("(封计划)出借计划时没有可以使用账号");
        }
        // 封计划前先处理需要处理计划绑定没有满额的标
        List<BidForPlanVO> bidForPlanVOs = planBidDao.getPlanBindBids(planId, false);
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
                    String smsPattern = String.format("(封计划时)账户余额不足，需出借金额：%s，,请及时处理(userId:%s)", investAmount, userId);
                    LOGGER.warn(smsPattern);
                    String phone = Config.get("closure.plan.account.notify");
                    planBidHandler.sendSms(smsPattern, phone);
                }
                throw e;
            }
        }
        // 释放计划绑定的标 code here ......
        planBidDao.unBindPlanBids(planId);
        // 封计划
        planService.termination(planId, pmsUserId);
    }

    @Transactional
    @Override
    public boolean doInvestPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId) throws Exception {
        InvestPlan investPlan = tradeDao.getInvestPlan(planId, 1);
        boolean isSuccess = true;
        if (investPlan.getStatus() != 4) {
            LOGGER.error("出借计划的状态不为出借中,不能出借.......");
            throw new BusinessException(ResponseCode.BID_FULLED); // 该项目已满额，请出借其他项目
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
        // 当为特殊账号时可以购买任何金额
        if (amount.compareTo(min) < 0) {
            LOGGER.error("投计划金额不能低于最低出借额 planId：{}, amount:{}, minAmount:{}", planId, amount, min);
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_LT100); // 投标金额不能低于最低起投金额
        }
        // 查询是否有逾期未还
        int count = planBidDao.getYqCount(accountId);
        if (count > 0) {
            LOGGER.error("投计划时，存在未还记录，不能出借userId: {}", accountId);
            throw new BusinessException(ResponseCode.BIT_LOAN_OVERDUE); //借款逾期未还，不能投标！
        }
        // 当该计划中绑定该用户的借款标时，改用户不能出借该计划
        BidForPlanVO bidForPlanVO = planBidDao.thisUserBorrowBidInPlan(planId, accountId);
        if (bidForPlanVO != null) {
            LOGGER.error("用户：{}出借投计划:{}时，其借款标在该计划中，不能出借.....", accountId, planId);
            throw new BusinessException(ResponseCode.BID_BORROWER_CANNOT_INVESTMENT);
        }
        Timestamp nowDate = tradeDao.getDBCurrentTime();
        // 验证返现券是否符合条件
        List<UserRedPacketInfo> userRedPacketInfos = planBidHandler.checkRedPackets(investPlan, fxhbIds, amount, nowDate);
        // 验证加息券是否符合条件
        planBidHandler.checkCoupon(investPlan, investPlan.getCycleType(), investPlan.getCycle(), jxqId, accountId, amount, nowDate);
        // 获取计划绑定且出借中状态的标
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
            LOGGER.error("出借计划后短信和站内信异常", t);
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
     * 添加计划出借记录
     *
     * @param userId
     * @param planId
     * @param amount
     * @return
     */
    private int insertRecordForInvestPlan(int userId, int planId, BigDecimal amount, BigDecimal freezeAmount) {
        int id = planDao.insertRecordForInvestPlan(userId, planId, amount,freezeAmount);
        if (id == 0) {
            LOGGER.error("添加计划出借记录失败,userId=[{}],investPlanId=[{}]", userId, planId);
            throw new BusinessException(ResponseCode.BID_INVESTMENT_FAILURE);    //添加计划出借记录失败
        }
        return id;
    }

}
