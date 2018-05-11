package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.dao.coupon.CouponDao;
import com.fenlibao.p2p.dao.plan.PlanBidDao;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.bid.InvestPlan;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.plan.PlanType;
import com.fenlibao.p2p.model.enums.bid.BidTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.bidinfo.BidTypeVO;
import com.fenlibao.p2p.model.vo.plan.BidForPlanVO;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author zeronx on 2017/11/20 9:37.
 * @version 1.0
 */
@Component
public class PlanBidHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanBidHandler.class);

    @Resource
    private PlanBidDao planBidDao;

    @Resource
    private CouponDao couponDao;
    @Resource
    private RedpacketService redpacketService;

    @Transactional(rollbackFor = Exception.class)
    public void releasePlan(PlanType planType, BigDecimal sumAmount, List<BidForPlanVO> tempBindBids) {
        Map<String, Object> paramMaps = new HashMap<>();
        // 发布计划的金额为关联的标的总额
        planType.setAmount(sumAmount);
        // 插入要发布的计划状态为已审核
        PlanType planTypeTemp = planBidDao.addInvestPlan(planType);
        paramMaps.put("planId", planTypeTemp.getId());
        paramMaps.put("isBind", 1);
        paramMaps.put("bindBids", tempBindBids);
        // 批量更新，计划绑定标的
        planBidDao.updateProductLib(paramMaps);
        // 发布计划
        planBidDao.releaseInvestPlan(planTypeTemp.getId());
    }

    /**
     * 校验红包
     * @param fxhbIds
     * @param amount
     * @param nowDate
     * @return
     */
    public List<UserRedPacketInfo> checkRedPackets(InvestPlan investPlan, String fxhbIds, BigDecimal amount, Timestamp nowDate) throws Exception {
        if (StringUtils.isNotBlank(fxhbIds)) {
            // 非新手标才可以使用返现红包
            if (investPlan.getIsNovice() == 1) {
                LOGGER.info(String.format("非新手标才可以使用返现红包 id：%s", investPlan.getId()));
                throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_FXHB);
            }
            String cycleType = investPlan.getCycleType();
            int cycle = investPlan.getCycle();
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
                    Date validDate = DateUtil.getLastTimeOfToday(redPacketInfo.getTimestamp());
                    if (validDate.compareTo(nowDate) < 0) {
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
        return null;
    }

    /**
     * //校验加息券
     * @param cycleType
     * @param cycle
     * @param jxqId
     * @param accountId
     * @param amount
     * @param nowDate
     * @throws Exception
     */
    public void checkCoupon(InvestPlan investPlan, String cycleType, int cycle, String jxqId, int accountId, BigDecimal amount, Timestamp nowDate) throws Exception {
        if (StringUtils.isNotBlank(jxqId)) {
            // 非新手标才可以使用加息券
            if (investPlan.getIsNovice() == 1) {
                LOGGER.info(String.format("非新手标才可以使用加息券 id：%s", investPlan.getId()));
                throw new BusinessException(ResponseCode.BID_NOVICE_NOT_USE_JXQ);
            }
            int jxqIdInt = Integer.parseInt(jxqId);
            //获取加息券
            UserCouponInfo userCouponInfo = couponDao.getCoupon(jxqIdInt, accountId);
            //金额，期限，投资类型,是否使用，是否过期
            if (userCouponInfo == null) {
                LOGGER.error("获取不到加息券ID:{}, userId:{}", jxqId, accountId);
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
                    LOGGER.info("加息券使用条件不满足");
                    throw new BusinessException(ResponseCode.BID_COUPON_CONDITIONS_NOT_SATISFIED);
                }
            }

            if (Integer.valueOf(userCouponInfo.getStatus()) == 2) {
                throw new BusinessException(ResponseCode.BID_COUPON_IS_USED);    //已经使用
            }

            Date validDate = DateUtil.getLastTimeOfToday(userCouponInfo.getValidTime());
            if (validDate.compareTo(nowDate) < 0) {
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
    }

}
