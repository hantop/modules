package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.framework.service.exception.LogicalException;
import com.fenlibao.p2p.dao.EnumDao;
import com.fenlibao.p2p.dao.bid.PlanDao;
import com.fenlibao.p2p.dao.coupon.CouponDao;
import com.fenlibao.p2p.dao.plan.PlanInfoDao;
import com.fenlibao.p2p.dao.trade.ITradeDao;
import com.fenlibao.p2p.model.entity.TEnum;
import com.fenlibao.p2p.model.entity.UserAccount;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.BidInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.bid.Plan;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.finacing.PlanInvestInfo;
import com.fenlibao.p2p.model.entity.plan.InvestPlanInfo;
import com.fenlibao.p2p.model.entity.plan.PlanRecordInfo;
import com.fenlibao.p2p.model.entity.plan.UserPlan;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.BidTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.InvestPlanStatus;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.bid.PlanBidVO;
import com.fenlibao.p2p.model.vo.bidinfo.BidTypeVO;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.PlanExtService;
import com.fenlibao.p2p.service.bid.PlanService;
import com.fenlibao.p2p.service.bid.migrate.impl.TenderOrderExecutor;
import com.fenlibao.p2p.service.pms.user.PmsUserService;
import com.fenlibao.p2p.service.privateMessage.PrivateMessageService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.service.xinwang.account.XWFundService;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.loader.Sender;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by LouisWang on 2015/8/14.
 */
@Service
public class PlanServiceImpl implements PlanService {
    private static final Logger logger = LogManager.getLogger(PlanServiceImpl.class);
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private PlanExtService planExtService;
    @Resource
    private RedpacketService redpacketService;
    @Resource
    private TenderOrderExecutor tenderOrderExecutor;
    @Resource
    private PmsUserService pmsUserService;

    @Resource
    private PlanDao planDao;

    @Resource
    private ITradeDao tradeDao;
    @Resource
    private CouponDao couponDao;
    @Resource
    private PlanInfoDao planInfoDao;
    @Resource
    private EnumDao enumDao;
    @Resource
    private SqlSession sqlSession;
    @Resource
    private PrivateMessageService privateMessageService;

    @Resource
    private XWUserInfoService xwUserInfoService;
    @Resource
    private XWFundService xwFundService;

    @Transactional
    @Override
    public void termination(int planId, int operatorId) {
        InvestPlanInfo planInfo = planInfoDao.getPlanInfo(planId);
        // 计算到期时间
        Date expireTime = buildExpireTime(planInfo);
        // 计算起息日期
        Date bearrateDate = buildBearrateDate(planInfo);
        Date nowTime = DateUtil.nowDate();
        // 修改计划筹满时间为当前时间
        planInfoDao.updatePlanTermination(planId, expireTime, bearrateDate, InvestPlanStatus.REFUNDING.getKey(), nowTime, nowTime);
        // 新增计划_终止记录
        planInfoDao.insertPlanTermination(planId, operatorId);
    }

    private Date buildBearrateDate(InvestPlanInfo planInfo) {
        Calendar calendar = Calendar.getInstance();
        // 起息日期
        calendar.setTime(DateUtil.nowDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 计算到期时间
     * @return
     * @param planInfo
     */
    private Date buildExpireTime(InvestPlanInfo planInfo) {
        final Date interestDate = DateUtil.nowDate();
        final Date endDate;
        if(planInfo.getCycleType().equals("d")) {
            endDate = DateUtil.dateAdd(interestDate, planInfo.getCycle());
        } else {
            endDate = DateUtil.monthAdd(interestDate, planInfo.getCycle());
        }
        // 转换为无时间的日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    @Override
    public int getPmsUserId() {
        String pmsUsername = "admin";
        TEnum enumOperator = enumDao.getEnum("t_plan_termination", "operater_id", "operater_username");
        if(enumOperator != null && StringUtils.isNotBlank(enumOperator.getEnumValue())) {
            pmsUsername = enumOperator.getEnumValue();
        }
        return pmsUserService.getUserId(pmsUsername);
    }

    @Transactional
    @Override
    public void doPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable {
        Plan plan = tradeDao.getPlan(planId);
        doPlan(plan, amount, accountId, fxhbIds, jxqId, experFlg);
    }

    @Transactional
    @Override
    public void doPlan(Plan plan, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable {
        //限制每个用户同一次投标频率为1分钟
        int lastOrderTime = userInfoService.getUserPlanLastOrder(accountId, plan.getId());
        if (lastOrderTime != 0 && lastOrderTime < 60) {
            throw new LogicalException(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode()); //投标过于频繁
        }
        BigDecimal min = new BigDecimal(InterfaceConst.BID_MIX_AMOUNT);
        if (amount.compareTo(min) < 0) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_LT100); //投标金额不能低于最低起投金额
        }

        List<UserRedPacketInfo> userRedPacketInfos = null;
        if (StringUtils.isNotBlank(fxhbIds) || StringUtils.isNotBlank(jxqId)) {
            Timestamp nowDate = tradeDao.getDBCurrentTime();
            //验证返现券是否符合条件
            if (StringUtils.isNotBlank(fxhbIds)) {
                // 非新手标才可以使用返现红包
                if ("S".equalsIgnoreCase(plan.getIsNoviceBid())) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_FXHB);
                }
                userRedPacketInfos = checkRedPackets(plan.getCycleType(), plan.getCycle(), fxhbIds, amount, nowDate);
            }
            // 验证加息券是否符合条件
            if (StringUtils.isNotBlank(jxqId)) {
                // 非新手标才可以使用加息券
                if ("S".equalsIgnoreCase(plan.getIsNoviceBid())) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_JXQ);
                }
                checkCoupon(plan.getCycleType(), plan.getCycle(), Integer.parseInt(jxqId), accountId, amount, nowDate);
            }
        }

        //检查用户余额，是否满足
        String accountType = InterfaceConst.ACCOUNT_TYPE_WLZH;
        UserAccount userAccount = userInfoService.getUserAccount(String.valueOf(accountId), accountType);
        if (userAccount.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT); //用户往来账户余额不足
        }
        tradeDao.lockPlan(plan.getId());
        //检查计划可投资余额，是否满足
        List<BidInfoForPlan> planSurplusBidList = tradeDao.getPlanSurplusBidList(plan.getId());
        BigDecimal surolusAmountSum = BigDecimal.ZERO;
        for (BidInfoForPlan bidInfoForPlan : planSurplusBidList) {
            surolusAmountSum = surolusAmountSum.add(bidInfoForPlan.getSurplusAmount());
        }
        if (surolusAmountSum.compareTo(amount) < 0) {
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //投资金额已超过剩余可投金额
        }
        //匹配买标计划
        List<PlanBidVO> planBidVOList = matchBid(planSurplusBidList, amount);
        if (planBidVOList == null || planBidVOList.size() == 0) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_FAILURE); //投资失败
        }
        //添加计划投资记录
        int planRecordId = tradeDao.addPlanRecord(plan.getId(), accountId);
        //循环买标，并添加计划的具体标投资记录
        Connection connection = sqlSession.getConnection();
        try {
            for (PlanBidVO planBidVO : planBidVOList) {
                int bidRecordId = planExtService.doBidForPlan(connection, planBidVO, accountId);
                if (bidRecordId > 0) {
                    tradeDao.addPlanBidRecord(planRecordId, bidRecordId);
                }
            }
        } catch (Throwable t) {
            rollback(connection);
        }
        //更新计划投资的金额和已投金额
        tradeDao.updatePlanRecordAmount(planRecordId);

        BigDecimal investedAmount = tradeDao.getPlanRecordAmount(planRecordId);
        if (investedAmount.compareTo(amount) != 0) {
            rollback(connection);
        }

        //获取用户信息
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", accountId);
        UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(userMap);

        //使用返现券
        if (!StringUtils.isBlank(fxhbIds)) {
            try{
                // modify by zeronx 21017-11-29 15：31 remove this method
                planExtService.useRedPackets(0, userRedPacketInfos, plan.getId(), planRecordId, userInfo, fxhbIds, InterfaceConst.FXHB_INVEST_TYPE_CUSPLAN, VersionTypeEnum.PT);
            }catch (Exception e){
                logger.error(e, e);
            }
        }

        //使用加息券
        if (!StringUtils.isBlank(jxqId)) {
            try{
                planExtService.useCoupon(planRecordId, userInfo, jxqId, InterfaceConst.FXHB_INVEST_TYPE_CUSPLAN);
            }catch (Exception e){
                logger.error(e, e);
            }
        }

        try {
            //检查封计划,更新状态
            tradeDao.updatePlanStatus(plan.getId());
        } catch (Exception e) {
            logger.error("检查封计划,更新状态异常", e);
        }

        //统计信息，发送短信和站内信
        try {
            String content = Sender.get("sms.dobid.content").replace("#{bidTitle}", plan.getTitle()).replace("#{investSum}", investedAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            planExtService.sendSmsAndLetter(userInfo.getPhone(), String.valueOf(accountId), content, VersionTypeEnum.PT);
        } catch (Throwable t) {
            logger.error("投资计划后短信和站内信异常", t);
        }
    }

    /**
     * 配标
     *
     * @param planSurplusBidList
     * @param amount
     * @return
     */
    private List<PlanBidVO> matchBid(List<BidInfoForPlan> planSurplusBidList, BigDecimal amount) {
        List<PlanBidVO> tarPlanBidVOList = new ArrayList<PlanBidVO>();
        BigDecimal surplusAmount = amount;
        boolean flag = false;
        for (int i = 0; i < planSurplusBidList.size(); i++) {
            BidInfoForPlan bidInfoForPlan = planSurplusBidList.get(i);
            if (flag) {
                //匹配
                PlanBidVO planBidVO = new PlanBidVO();
                planBidVO.setBidId(bidInfoForPlan.getBidId());
                planBidVO.setBidName(bidInfoForPlan.getBidTitle());
                if (bidInfoForPlan.getSurplusAmount().compareTo(surplusAmount) <= 0) {
                    planBidVO.setPurchaseAmount(bidInfoForPlan.getSurplusAmount());
                } else {
                    planBidVO.setPurchaseAmount(surplusAmount);
                }

                //如果匹配的标金额小于100，调整购买金额
                if (planBidVO.getPurchaseAmount().compareTo(new BigDecimal(100)) < 0) {
                    boolean balanceFlag = false;
                    BigDecimal amountDiff = new BigDecimal(100).subtract(planBidVO.getPurchaseAmount());
                    for (int j = tarPlanBidVOList.size() - 1; j >= 0; j--) {
                        PlanBidVO planBidVOTemp = tarPlanBidVOList.get(j);
                        if (planBidVOTemp.getPurchaseAmount().subtract(amountDiff).compareTo(new BigDecimal(100)) >= 0) {
                            surplusAmount = surplusAmount.add(amountDiff);
                            planBidVOTemp.setPurchaseAmount(planBidVOTemp.getPurchaseAmount().subtract(amountDiff));
                            tarPlanBidVOList.remove(j);
                            tarPlanBidVOList.add(j, planBidVOTemp);
                            balanceFlag = true;
                            break;
                        }
                    }
                    if (balanceFlag) {
                        i--;
                        continue;
                    }
                }
                tarPlanBidVOList.add(planBidVO);

                surplusAmount = surplusAmount.subtract(planBidVO.getPurchaseAmount());
                if (surplusAmount.compareTo(BigDecimal.ZERO) == 1) {//还有余额
                    continue;
                } else { //正好配完或余额小于标的
                    break;
                }
            } else {
                //寻址
                int bg = bidInfoForPlan.getSurplusAmount().setScale(0, BigDecimal.ROUND_DOWN).compareTo(amount.setScale(0, BigDecimal.ROUND_DOWN));
                if (bg == -1) {
                    i = (i >= 1) ? i - 2 : i - 1;
                    flag = true;
                    continue;
                }
                //正好匹配到或者已经到达最后
                if (bg == 0 || i == planSurplusBidList.size() - 1) {
                    i--;
                    flag = true;
                    continue;
                }
            }
        }
        return tarPlanBidVOList;
    }

    /**
     * 获取计划
     *
     * @param planId
     * @return
     */
    public Plan getPlan(int planId) {
        return tradeDao.getPlan(planId);
    }

    /**
     * 获取计划
     *
     * @param planId
     * @return
     */
    public InvestPlan getInvestPlan(int planId) {
        return tradeDao.getInvestPlan(planId, 0);
    }

    /**
     * 校验红包
     *
     * @param fxhbIds
     * @param amount
     * @param nowDate
     * @return
     */
    private List<UserRedPacketInfo> checkRedPackets(String cycleType, int cycle, String fxhbIds, BigDecimal amount, Timestamp nowDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        String[] hbIdArr = fxhbIds.split("\\|");
        params.put("fxhbIdArr", hbIdArr);
        List<UserRedPacketInfo> userRedPacketInfos = null;
        try {
            userRedPacketInfos = redpacketService.getBidRedpacket(params);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_NOT_EXIST);
        }
        if (userRedPacketInfos == null || userRedPacketInfos.size() == 0 || userRedPacketInfos.size() != hbIdArr.length) {
            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_NOT_EXIST);
        }

        Map<String, UserRedPacketInfo> redPacketInfoMap = new HashMap<String, UserRedPacketInfo>();
        for (UserRedPacketInfo redPacketInfo : userRedPacketInfos) {
            //金额，期限，投资类型,是否使用，是否过期
            if (redPacketInfo != null) {
                /**
                 * 校验返现红包的使用规则
                 * 1.是否有对应红包；2.返现红包是否使用过；3.返现红包是否已经过期；
                 * 4.投资金额<返现红包最少投资金额；5.投资金额能获取的返现红包数量。
                 * 6.投资期限；7.标类型
                 */
                //投资类型
                List<BidTypeVO> bidTypeVOList = redPacketInfo.getBidTypes();
                if (bidTypeVOList != null && bidTypeVOList.size() != 0) {
                    boolean flag = false;
                    for (BidTypeVO bidTypeVO : bidTypeVOList) {
                        if (BidTypeEnum.JH.getCode().equalsIgnoreCase(bidTypeVO.getTypeCode())) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED);
                    }
                }

                BigDecimal leastMoney = redPacketInfo.getConditionBalance();
                if (Integer.valueOf(redPacketInfo.getStatus()) == 2) {
                    throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_IS_USED);    //返现红包已经使用
                }

                if (redPacketInfo.getTimestamp().compareTo(nowDate) < 0) {
                    throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_IS_OVERDUE);  //返现红包已经过期
                }

                Integer investDay = "d".equalsIgnoreCase(cycleType) ? cycle : cycle * 30;
                if (redPacketInfo.getInvestDeadline() != null && investDay < redPacketInfo.getInvestDeadline()) {
                    throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED);//投资期限
                }

                if (redPacketInfoMap.size() > 0) {
                    if (amount.compareTo(leastMoney) < 0) {
                        throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED);
                    } else {
                        BigDecimal fxhbTotail = new BigDecimal(0);
                        Iterator entries = redPacketInfoMap.entrySet().iterator();
                        fxhbTotail = fxhbTotail.add(leastMoney);
                        while (entries.hasNext()) {
                            Map.Entry entry = (Map.Entry) entries.next();
                            UserRedPacketInfo packetInfoTmp = (UserRedPacketInfo) entry.getValue();
                            BigDecimal dkLeastMoney = packetInfoTmp.getConditionBalance();
                            fxhbTotail = fxhbTotail.add(dkLeastMoney);
                        }
                        if (amount.compareTo(fxhbTotail) < 0) {
                            throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED);  //返现红包使用不符合规则
                        } else {
                            redPacketInfoMap.put(String.valueOf(redPacketInfo.getHbId()), redPacketInfo);
                        }
                    }
                } else {
                    if (amount.compareTo(leastMoney) < 0) {
                        throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED); //投标金额不能使用多少钱的返现红包
                    } else {
                        redPacketInfoMap.put(String.valueOf(redPacketInfo.getHbId()), redPacketInfo);
                    }
                }

            } else {
                throw new BusinessException(ResponseCode.BID_RED_ENVELOPE_NOT_EXIST);  //返现红包不存在
            }
        }
        return userRedPacketInfos;
    }

    /**
     * 校验加息券
     *
     * @param jxqId
     * @param accountId
     * @param amount
     * @param nowDate
     */
    private void checkCoupon(String cycleType, int cycle, int jxqId, int accountId, BigDecimal amount, Timestamp nowDate) {
        //获取加息券
        UserCouponInfo userCouponInfo = couponDao.getCoupon(jxqId, accountId);

        //金额，期限，投资类型,是否使用，是否过期
        if (userCouponInfo == null) {
            throw new BusinessException(ResponseCode.BID_COUPON_NOT_EXIST);
        }

        //投资类型
        List<BidTypeVO> bidTypeVOList = userCouponInfo.getBidTypes();
        if (bidTypeVOList != null && bidTypeVOList.size() != 0) {
            boolean flag = false;
            for (BidTypeVO bidTypeVO : bidTypeVOList) {
                if (BidTypeEnum.JH.getCode().equalsIgnoreCase(bidTypeVO.getTypeCode())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new BusinessException(ResponseCode.BID_COUPON_CONDITIONS_NOT_SATISFIED);
            }
        }


        if (Integer.valueOf(userCouponInfo.getStatus()) == 2) {
            throw new BusinessException(ResponseCode.BID_COUPON_IS_USED);    //已经使用
        }

        if (userCouponInfo.getValidTime().compareTo(nowDate) < 0) {
            throw new BusinessException(ResponseCode.BID_COUPON_IS_OVERDUE);  //已经过期
        }

        //金额
        BigDecimal maxInvestMoney = userCouponInfo.getMaxInvestMoney() == null ? BigDecimal.ZERO : userCouponInfo.getMaxInvestMoney(); //投资金额上限
        BigDecimal minInvestMoney = userCouponInfo.getMinInvestMoney() == null ? BigDecimal.ZERO : userCouponInfo.getMinInvestMoney(); //投资金额下限
        if (!((minInvestMoney.compareTo(BigDecimal.ZERO) == 0 && maxInvestMoney.compareTo(BigDecimal.ZERO) == 0)
                || (amount.compareTo(minInvestMoney) > -1 && amount.compareTo(maxInvestMoney) < 1)
                || (maxInvestMoney.compareTo(BigDecimal.ZERO) == 0 && amount.compareTo(minInvestMoney) > -1))) {
            throw new BusinessException(ResponseCode.BID_COUPON_CONDITIONS_NOT_SATISFIED);
        }

        //期限
        Integer maxInvestDay = userCouponInfo.getMaxInvestDay() == null ? 0 : userCouponInfo.getMaxInvestDay(); //投资期限上限(小于XX天)
        Integer minInvestDay = userCouponInfo.getMinInvestDay() == null ? 0 : userCouponInfo.getMinInvestDay(); //投资期限下限(大于XX天)
        Integer investDay = "d".equalsIgnoreCase(cycleType) ? cycle : cycle * 30;
        if (!((minInvestDay == 0 && maxInvestDay == 0) || (investDay >= minInvestDay && investDay <= maxInvestDay)
                || (maxInvestDay == 0 && investDay >= minInvestDay))) {
            throw new BusinessException(ResponseCode.BID_COUPON_CONDITIONS_NOT_SATISFIED);
        }
    }

    /**
     * 投资新版计划
     *
     * @param planId
     * @param amount
     * @param accountId
     * @param fxhbIds
     * @param jxqId
     * @param experFlg
     * @throws Throwable
     */
    @Transactional
    @Override
    public void doInvestPlan(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable {
        //限制每个用户同一次投标频率为1分钟
        int lastOrderTime = userInfoService.getUserInvestPlanLastOrder(accountId, planId);
        if (lastOrderTime != 0 && lastOrderTime < 60) {
            throw new LogicalException(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode()); //投标过于频繁
        }

        InvestPlan investPlan = tradeDao.getInvestPlan(planId, 1);

        BigDecimal min = new BigDecimal(InterfaceConst.BID_MIX_AMOUNT);
        if (amount.compareTo(min) < 0) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_LT100); //投标金额不能低于最低起投金额
        }

        List<UserRedPacketInfo> userRedPacketInfos = null;
        if (StringUtils.isNotBlank(fxhbIds) || StringUtils.isNotBlank(jxqId)) {
            Timestamp nowDate = tradeDao.getDBCurrentTime();
            //验证返现券是否符合条件
            if (StringUtils.isNotBlank(fxhbIds)) {
                // 非新手标才可以使用返现红包
                if (investPlan.getIsNovice() == 1) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_FXHB);
                }
                userRedPacketInfos = checkRedPackets(investPlan.getCycleType(), investPlan.getCycle(), fxhbIds, amount, nowDate);
            }
            // 验证加息券是否符合条件
            if (StringUtils.isNotBlank(jxqId)) {
                // 非新手标才可以使用加息券
                if (investPlan.getIsNovice() == 1) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_JXQ);
                }
                checkCoupon(investPlan.getCycleType(), investPlan.getCycle(), Integer.parseInt(jxqId), accountId, amount, nowDate);
            }
        }

        //检查用户余额，是否满足
        String accountType = InterfaceConst.ACCOUNT_TYPE_WLZH;
        UserAccount userAccount = userInfoService.getUserAccount(String.valueOf(accountId), accountType);
        if (userAccount.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT); //用户往来账户余额不足
        }

        //检查计划可投资余额，是否满足
        if (investPlan.getSurplusAmount().compareTo(amount) < 0) {
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //投资金额已超过剩余可投金额
        }

        // 扣减计划可投金额
        updateSurplusAmountForInvestPlan(investPlan, amount, accountId);
        // 冻结用户账户金额
        lockAmountForPlan(investPlan, amount, accountId, VersionTypeEnum.PT);
        // 增加计划投资记录
        int planRecordId = insertRecordForInvestPlan(accountId, investPlan.getId(), amount, null);

        //获取用户信息
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", accountId);
        UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(userMap);

        //使用返现券
        if (!StringUtils.isBlank(fxhbIds)) {
            try{
                // modify by zeronx 21017-11-29 15：31 remove this method
                // planExtService.useRedPackets(userRedPacketInfos, investPlan.getId(), planRecordId, userInfo, fxhbIds, InterfaceConst.FXHB_INVEST_TYPE_INVESTPLAN, VersionTypeEnum.PT);
            }catch (Exception e){
                logger.error(e, e);
            }
        }

        //使用加息券
        if (!StringUtils.isBlank(jxqId)) {
            try{
                planExtService.useCoupon(planRecordId, userInfo, jxqId, InterfaceConst.FXHB_INVEST_TYPE_INVESTPLAN);
            }catch (Exception e){
                logger.error(e, e);
            }
        }

        //统计信息，发送短信和站内信
        try {
            String content = Sender.get("sms.dobid.content").replace("#{bidTitle}", investPlan.getTitle()).replace("#{investSum}", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            planExtService.sendSmsAndLetter(userInfo.getPhone(), String.valueOf(accountId), content, VersionTypeEnum.PT);
        } catch (Throwable t) {
            logger.error("投资计划后短信和站内信异常", t);
        }

        try {
            //检查封计划,更新状态
            if(investPlan.getSurplusAmount().subtract(amount).subtract(new BigDecimal(100)).compareTo(BigDecimal.ZERO) < 0){
                planExtService.updateInvestPlanStatus(investPlan.getId());
            }
        } catch (Exception e) {
            logger.error("检查封计划,更新状态异常", e);
        }
    }

    /**
     * 投资新版计划
     *
     * @param planId
     * @param amount
     * @param accountId
     * @param fxhbIds
     * @param jxqId
     * @param experFlg
     * @throws Throwable
     */
    @Transactional
    @Override
    public void doInvestPlanForCG(int planId, BigDecimal amount, int accountId, String fxhbIds, String jxqId, String experFlg) throws Throwable {
        //限制每个用户同一次投标频率为1分钟
        int lastOrderTime = userInfoService.getUserInvestPlanLastOrder(accountId, planId);
        if (lastOrderTime != 0 && lastOrderTime < 60) {
            throw new LogicalException(ResponseCode.COMMON_OPERATION_IS_TOO_FREQUENT.getCode()); //投标过于频繁
        }

        InvestPlan investPlan = tradeDao.getInvestPlan(planId, 1);

        BigDecimal min = new BigDecimal(InterfaceConst.BID_MIX_AMOUNT);
        if (amount.compareTo(min) < 0) {
            throw new BusinessException(ResponseCode.BID_INVESTMENT_AMOUNT_LT100); //投标金额不能低于最低起投金额
        }

        List<UserRedPacketInfo> userRedPacketInfos = null;
        if (StringUtils.isNotBlank(fxhbIds) || StringUtils.isNotBlank(jxqId)) {
            Timestamp nowDate = tradeDao.getDBCurrentTime();
            //验证返现券是否符合条件
            if (StringUtils.isNotBlank(fxhbIds)) {
                // 非新手标才可以使用返现红包
                if (investPlan.getIsNovice() == 1) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_FXHB);
                }
                userRedPacketInfos = checkRedPackets(investPlan.getCycleType(), investPlan.getCycle(), fxhbIds, amount, nowDate);
            }
            // 验证加息券是否符合条件
            if (StringUtils.isNotBlank(jxqId)) {
                // 非新手标才可以使用加息券
                if (investPlan.getIsNovice() == 1) {
                    throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_JXQ);
                }
                checkCoupon(investPlan.getCycleType(), investPlan.getCycle(), Integer.parseInt(jxqId), accountId, amount, nowDate);
            }
        }

        //检查用户余额，是否满足
//        XWFundAccount xwUserInfo = xwUserInfoService.getFundAccount(accountId, SysFundAccountType.XW_INVESTOR_WLZH);
//        if (xwUserInfo == null || xwUserInfo.getAmount().compareTo(amount) < 0) {
//            throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT); //用户往来账户余额不足
//        }
        // modify :2017-08-24 zeronx
        XinwangUserInfo xinwangUserInfo = null;
        try {
            xinwangUserInfo = xwUserInfoService.queryUserInfo(UserRole.INVESTOR.getCode()+accountId);
        } catch (Exception e) {
            logger.error("调用新网直连查询用户信息异常：" + e.getMessage());
            e.printStackTrace();
        }
        // 直接调用新网查询用户信息接口 判断用户可用余额是否 >= amount
        if (xinwangUserInfo == null || xinwangUserInfo.getAvailableAmount().compareTo(amount) < 0) {
            throw new BusinessException(ResponseCode.USER_ACCOUNT_BALANCE_INSUFFICIENT); //用户往来账户余额不足
        }

         //检查计划可投资余额，是否满足
        if (investPlan.getSurplusAmount().compareTo(amount) < 0) {
            throw new BusinessException(ResponseCode.BID_INVEST_AMOUNT_TOO_MUCH); //投资金额已超过剩余可投金额
        }

        //获取用户信息
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", accountId);
        UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(userMap);

        // 扣减计划可投金额
        updateSurplusAmountForInvestPlan(investPlan, amount, accountId);
        // 冻结用户存管账户金额
        lockAmountForPlan(investPlan, amount, accountId, VersionTypeEnum.CG);
        //xwFundService.doFreezeFund(accountId, amount);
        // 增加计划投资记录
        int planRecordId = insertRecordForInvestPlan(accountId, investPlan.getId(), amount, amount);

        //使用返现券
        if (!StringUtils.isBlank(fxhbIds)) {
            try{
                // modify by zeronx 21017-11-29 15：31 remove this method
                planExtService.useRedPackets(0, userRedPacketInfos, investPlan.getId(), planRecordId, userInfo, fxhbIds, InterfaceConst.FXHB_INVEST_TYPE_INVESTPLAN, VersionTypeEnum.CG);
            }catch (Exception e){
                logger.error(e, e);
            }
        }

        //使用加息券
        if (!StringUtils.isBlank(jxqId)) {
           try{
               planExtService.useCoupon(planRecordId, userInfo, jxqId, InterfaceConst.FXHB_INVEST_TYPE_INVESTPLAN);
           }catch (Exception e){
               logger.error(e, e);
           }
        }

        //统计信息，发送短信和站内信
        try {
            String content = Sender.get("sms.dobid.content").replace("#{bidTitle}", investPlan.getTitle()).replace("#{investSum}", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            planExtService.sendSmsAndLetter(userInfo.getPhone(), String.valueOf(accountId), content, VersionTypeEnum.CG);
        } catch (Throwable t) {
            logger.error("投资计划后短信和站内信异常", t);
        }

        try {
            if(investPlan.getSurplusAmount().subtract(amount).subtract(new BigDecimal(100)).compareTo(BigDecimal.ZERO) < 0){
                planExtService.updateInvestPlanStatus(investPlan.getId());
            }
        } catch (Exception e) {
            logger.error("检查封计划,更新状态异常", e);
        }
    }

    @Override
    public Integer getPlanMonthNum(int ownsStatus, long interestTime, long exitTime) {
        int month = 0;
        for (int i = 1; i <= 12; i++) {

            long naturaDate = 0;
            try {
                naturaDate = DateUtil.rollNaturalMonth(interestTime, i);//起息时间经自然月推算到当前月经过了i个月得到准确日期
            } catch (Exception e) {

            }
            if (ownsStatus == 1 || ownsStatus == 2) {//持有中或者锁定期
                if (naturaDate >= new Date().getTime()) {//
                    month = i;
                    break;
                }
            } else {//已退出
                if (naturaDate >= exitTime) {//起息时间经自然月推算到退出日期经过了几个月
                    month = i;
                    break;
                }
            }

        }
        return month;
    }

    @Transactional
    @Override
    public Timestamp quitUserPlan(PlanRecordInfo planRecordInfo, BigDecimal expectedProfitPlan) {
        UserInfo userInfo = userInfoService.getUser(null,null,String.valueOf(planRecordInfo.getUserId()));
        //锁用户投资计划记录
        planDao.lockUserPlan(planRecordInfo.getUserPlanId());
        Timestamp timestamp = tradeDao.getDBCurrentTime();
        planDao.insertExitRecord(planRecordInfo.getUserPlanId(),timestamp);

        //统计信息，发送短信和站内信
        BigDecimal fee = BigDecimal.ZERO;
        Double feeStr = 0d;
        if (planRecordInfo.getCanQuit() != 1) {
            fee = expectedProfitPlan.add(new BigDecimal(planRecordInfo.getInvestAmount())).multiply(BigDecimal.valueOf(0.01));
            feeStr = fee.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        expectedProfitPlan = expectedProfitPlan.setScale(2, BigDecimal.ROUND_HALF_UP);

        //站内信和短信不能影响退出
        try {
            String content = Sender.get("sms.quitbid.content").replace("#{planTitle}", planRecordInfo.getPlanTitle())
                    .replace("#{investAmount}",String.valueOf(planRecordInfo.getInvestAmount())).replace("#{profit}",expectedProfitPlan.toString())
                    .replace("#{fee}",String.valueOf(feeStr));

            planExtService.sendMsg(userInfo.getPhone(), content);
            InvestPlan investPlan = tradeDao.getInvestPlanByUserPlanId(planRecordInfo.getUserPlanId());
            privateMessageService.sendLetter(userInfo.getUserId(), InterfaceConst.PLAN_QUIT_PREFIX, content, VersionTypeEnum.parse(investPlan.getIsCG()));
        } catch (Throwable t) {
            logger.error("申请退出计划后短信和站内信异常", t);
        }
        return timestamp;
    }

    @Override
    public List<PlanInvestInfo> getAllMyPlan(Integer userId, VersionTypeEnum vte, PageBounds pageBounds) {
        return planDao.getAllMyPlan(userId,vte,pageBounds);
    }

    @Override
    public double getNewPlanRate(PlanInvestInfo investInfo) {
        if (investInfo.getType() == 2) {
            return  investInfo.getYearYield();
        }

        //没有起息的
        if (investInfo.getInterestTime() == null && investInfo.getType() == 1) {
            return investInfo.getLowRate();
        }else {//已经起息的
            int month = com.fenlibao.p2p.util.DateUtil.monthsBetweenDates(investInfo.getInterestTime(),investInfo.getExitTime()==null?new Date():investInfo.getExitTime());
            double rate = investInfo.getLowRate()+ investInfo.getBonusRate() * month ;
            return rate;
        }
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
            logger.error("扣减计划可出借额失败,userId=[{}],investPlanId=[{}]", accountId, investPlan.getId());
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
        int id = planDao.insertRecordForInvestPlan(userId, planId, amount, freezeAmount);
        if (id == 0) {
            logger.error("添加计划投资记录失败,userId=[{}],investPlanId=[{}]", userId, planId);
            throw new BusinessException(ResponseCode.BID_INVESTMENT_FAILURE);    //添加计划投资记录失败
        }
        return id;
    }

    /**
     * 冻结用户金额
     *
     * @param investPlan
     * @param amount
     * @param accountId
     * @throws Throwable
     */
    private void lockAmountForPlan(InvestPlan investPlan, BigDecimal amount, int accountId, VersionTypeEnum versionTypeEnum) throws Throwable {
        Connection connection = sqlSession.getConnection();
        try {
            tenderOrderExecutor.lockAmountForPlan(connection, investPlan, accountId, amount, versionTypeEnum);
        } catch (Throwable t) {
            rollback(connection);
            throw t;
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        throw new BusinessException(ResponseCode.BID_INVESTMENT_FAILURE); //投资失败
    }

    @Override
    public List<PlanInvestInfo> getMyQuitPlan(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds) {
        return planDao.getMyQuitPlans(userId,versionTypeEnum,pageBounds);
    }

    @Override
    public List<PlanInvestInfo> getMySuccessPlans(Integer userId, VersionTypeEnum versionTypeEnum, PageBounds pageBounds) {
        return planDao.getMySuccessPlans(userId,versionTypeEnum,pageBounds);
    }

    @Override
    public boolean checkExitingPlan(int userPlanId) {
        UserPlan userPlan = planDao.getExitPlan(userPlanId);
        if (userPlan != null) {
            return true;
        }
        return false;
    }
}