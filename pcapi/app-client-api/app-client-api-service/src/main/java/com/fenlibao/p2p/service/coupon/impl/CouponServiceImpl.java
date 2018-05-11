package com.fenlibao.p2p.service.coupon.impl;

import com.fenlibao.p2p.dao.coupon.CouponDao;
import com.fenlibao.p2p.model.entity.coupons.RateCoupon;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;
import com.fenlibao.p2p.model.entity.coupons.UserCouponStatisticsInfo;
import com.fenlibao.p2p.model.enums.bid.BidTypeEnum;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.bidinfo.BidTypeVO;
import com.fenlibao.p2p.service.coupon.CouponService;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/10/16.
 */
@Service
public class CouponServiceImpl implements CouponService {
    private static final Logger logger= LogManager.getLogger(CouponServiceImpl.class);
    @Resource
    private CouponDao couponDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> grantRateCouponFirstLogin(String phoneNum, String userId) throws Exception {
        List<String> scorePercents = new LinkedList<>();
        try {
            // 注册活动编码
            String registerActivitycode = Config.get("register.activitycode");
            List<RateCoupon> rateCoupons = this.getRateCoupons(registerActivitycode);
            if (rateCoupons != null && rateCoupons.size() > 0) {
                // 加息幅度格式
                DecimalFormat scorePercentDF = new DecimalFormat("###.##");
                String percentStr = "%";
                // 加息幅度
                BigDecimal score;
                // 加息幅度转为百分比
                String scorePercent;
                for (RateCoupon rateCoupon : rateCoupons) {
                    // 加息幅度转为百分比
                    score = rateCoupon.getScope().multiply(new BigDecimal(100));
                    scorePercent = scorePercentDF.format(score).concat(percentStr);
                    scorePercents.add(scorePercent);
                }
                this.addUserRateCoupons(rateCoupons, Integer.valueOf(userId));
            }
        } catch (Exception e) {
            String message = "[首次登陆发放注册加息券异常：]" + e.getMessage();
            logger.error(message, e);
            throw new Exception();
        }
        return scorePercents;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> grantRateCouponRegister(String phoneNum, String userId) throws Exception {
        List<String> scorePercents = new LinkedList<>();
        try {
            // 注册活动编码
            String registerActivitycode = Config.get("register.activitycode");
            List<RateCoupon> rateCoupons = this.getRateCoupons(registerActivitycode);
            if (rateCoupons != null && rateCoupons.size() > 0) {
                // 加息幅度格式
                DecimalFormat scorePercentDF = new DecimalFormat("###.##");
                String percentStr = "%";
                // 加息幅度
                BigDecimal score;
                // 加息幅度转为百分比
                String scorePercent;
                for (RateCoupon rateCoupon : rateCoupons) {
                    // 加息幅度转为百分比
                    score = rateCoupon.getScope().multiply(new BigDecimal(100));
                    scorePercent = scorePercentDF.format(score).concat(percentStr);
                    scorePercents.add(scorePercent);
                }
                this.addUserRateCoupons(rateCoupons, Integer.valueOf(userId));
            }
        } catch (Exception e) {
            String message = "[注册加息券异常：]" + e.getMessage();
            logger.error(message, e);
            throw new Exception();
        }
        return scorePercents;
    }

    @Override
    public void addUserRateCoupons(List<RateCoupon> rateCoupons, Integer userId) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        long nowMiniSecond = startTime.getTimeInMillis();
        long endMiniSecond;
        List<UserCouponInfo> userCouponInfos = new LinkedList<>();
        UserCouponInfo userCouponInfo;
        for (RateCoupon rateCoupon : rateCoupons) {
            userCouponInfo = new UserCouponInfo();
            userCouponInfo.setActivityId(rateCoupon.getActivityId());
            userCouponInfo.setCouponId(rateCoupon.getId());
            userCouponInfo.setUserId(userId);
            endMiniSecond = nowMiniSecond + ((long) rateCoupon.getEffectDay() * 24 * 3600 * 1000) - 1000;
            userCouponInfo.setValidTime(new Timestamp(endMiniSecond));
            userCouponInfos.add(userCouponInfo);
        }
        couponDao.addUserRateCoupons(userCouponInfos);
    }

    @Override
    public List<RateCoupon> getRateCoupons(String activityCode) {
        return couponDao.getRateCoupons(activityCode);
    }

    @Override
    public List getCoupons(Map<String, Object> paramMap) {
        List<UserCouponInfo> userCouponInfos = couponDao.getCoupons(paramMap);
        List couponInfos = new ArrayList();
        Map<String,Object> couponMap;
        List<BidTypeVO> bidTypes;
        StringBuffer bidTypeNames;
        for (UserCouponInfo info : userCouponInfos) {
            couponMap = new HashMap<>(10);
            couponMap.put("recordId",info.getId());
            couponMap.put("status",paramMap.get("status"));
            DecimalFormat df = new DecimalFormat("0.0000") ;
            couponMap.put("interestRate",df.format(info.getScope()));
            couponMap.put("validityDate",info.getValidTime().getTime()/1000);

            couponMap.put("maxInvestMoney", info.getMaxInvestMoney().setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
            couponMap.put("minInvestMoney", info.getMinInvestMoney().setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
            couponMap.put("maxInvestDay", info.getMaxInvestDay());
            couponMap.put("minInvestDay", info.getMinInvestDay());
            couponMap.put("overdueFlag", info.getOverdueFlag()==1?true:false);

            String deadlineTips=info.getMinInvestDay()!= null?
                                 info.getMaxInvestDay()!= null?
                                    String.format(InterfaceConst.COUPON_JXJ_DEADLINE_TIPS_AND, info.getMinInvestDay(), info.getMaxInvestDay())
                                    : String.format(InterfaceConst.INVESTMENT_DEADLINE_TIPS, info.getMinInvestDay())
                                :info.getMaxInvestDay()!= null?
                                    String.format(InterfaceConst.COUPON_JXJ_DEADLINE_TIPS, info.getMaxInvestDay())
                                    : InterfaceConst.INVESTMENT_DEADLINE_TIPS_ALL;
            couponMap.put("deadlineTips", deadlineTips);

            bidTypes = info.getBidTypes();
            if (bidTypes == null || bidTypes.size() == 0 || bidTypes.size() == BidTypeEnum.length()) {
                couponMap.put("bidTypeTips", InterfaceConst.INVESTMENT_BIDTYPE_TIPS_ALL);
            } else {
                bidTypeNames = new StringBuffer(4);
                for (BidTypeVO type : bidTypes) {
                    bidTypeNames.append(type.getTypeName() + "/");
                }
                bidTypeNames.deleteCharAt(bidTypeNames.length() - 1);
                couponMap.put("bidTypeTips", String.format(InterfaceConst.INVESTMENT_BIDTYPE_TIPS, bidTypeNames));
            }
            couponInfos.add(couponMap);
        }

        return couponInfos;
    }


    @Override
    public List<UserCouponInfo> getAddinterestList(Map<String, Object> paramMap) {

        List<UserCouponInfo> userCouponInfos = couponDao.getAddinterestList(paramMap);
        List couponInfos = new ArrayList();
        Map<String,Object> couponMap;
        List<BidTypeVO> bidTypes;
        StringBuffer bidTypeNames;
        for (UserCouponInfo info : userCouponInfos) {
            couponMap = new HashMap<>(10);
            couponMap.put("recordId",info.getId());
            DecimalFormat df = new DecimalFormat("0.0000") ;
            couponMap.put("interestRate",df.format(info.getScope()));
            couponMap.put("validityDate",info.getValidTime().getTime()/1000);
            couponMap.put("status",1);
            couponMap.put("maxInvestMoney", info.getMaxInvestMoney().setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
            couponMap.put("minInvestMoney", info.getMinInvestMoney().setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
            couponMap.put("maxInvestDay", info.getMaxInvestDay());
            couponMap.put("minInvestDay", info.getMinInvestDay());
            couponMap.put("overdueFlag", info.getSurplusDays()< InterfaceConst.OVERDUE?true:false);

            String deadlineTips=info.getMinInvestDay()!= null?
                    info.getMaxInvestDay()!= null?
                            String.format(InterfaceConst.COUPON_JXJ_DEADLINE_TIPS_AND, info.getMinInvestDay(), info.getMaxInvestDay())
                            : String.format(InterfaceConst.INVESTMENT_DEADLINE_TIPS, info.getMinInvestDay())
                    :info.getMaxInvestDay()!= null?
                    String.format(InterfaceConst.COUPON_JXJ_DEADLINE_TIPS, info.getMaxInvestDay())
                    : InterfaceConst.INVESTMENT_DEADLINE_TIPS_ALL;
            couponMap.put("deadlineTips", deadlineTips);

            bidTypes = info.getBidTypes();
            if (bidTypes == null || bidTypes.size() == 0 || bidTypes.size() == BidTypeEnum.length()) {
                couponMap.put("bidTypeTips", InterfaceConst.INVESTMENT_BIDTYPE_TIPS_ALL);
            } else {
                bidTypeNames = new StringBuffer(4);
                for (BidTypeVO type : bidTypes) {
                    bidTypeNames.append(type.getTypeName() + "/");
                }
                bidTypeNames.deleteCharAt(bidTypeNames.length() - 1);
                couponMap.put("bidTypeTips", String.format(InterfaceConst.INVESTMENT_BIDTYPE_TIPS, bidTypeNames));
            }
            couponInfos.add(couponMap);
        }
        return couponInfos;
    }


    @Override
    public int getCouponsCount(Map<String, Object> paramMap) {
        return couponDao.getCouponsCount(paramMap);
    }

    /**
     * 根据参数获取可使用优惠劵数量
     *
     * @param paramMap
     * @return
     */
    public int getMyConponsCount(Map<String, Object> paramMap){
        return couponDao.getMyConponsCount(paramMap);
    }

    @Override
    public int updateUserCoupon(Map<String, Object> paramMap) {
        return couponDao.updateUserCoupon(paramMap);
    }

    /**
     * 加息券统计
     *
     * @param paramMap
     */
    public UserCouponStatisticsInfo getUserCouponStatistics(Map<String, Object> paramMap){
        return couponDao.getUserCouponStatistics(paramMap);
    }
}
