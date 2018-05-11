package com.fenlibao.p2p.service.coupon;

import com.fenlibao.p2p.model.entity.coupons.RateCoupon;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;

import java.util.List;
import java.util.Map;

/**
 * 优惠劵
 * Created by junda.feng
 */
public interface CouponService {
    /**
     * 首次登陆发送注册加息券
     *
     * @param phoneNum
     * @param userId
     */
    List<String> grantRateCouponFirstLogin(String phoneNum, String userId) throws Exception;

    /**
     * 发送注册加息券
     *
     * @param phoneNum
     * @param userId
     */
    List<String> grantRateCouponRegister(String phoneNum, String userId) throws Exception;

    /**
     * 增加用户加息券
     *
     * @param rateCoupons
     * @param userId
     */
    void addUserRateCoupons(List<RateCoupon> rateCoupons, Integer userId);

    /**
     * 根据活动编码获取加息券
     *
     * @param activityCode
     * @return
     */
    List<RateCoupon> getRateCoupons(String activityCode);

    /**
     * 根据用户ID获取优惠劵
     * @param paramMap
     * @return
     */
    List<UserCouponInfo> getCoupons(Map<String, Object> paramMap);

    /**
     * 根据用户ID获取优惠劵-投资使用
     * @param paramMap
     * @return
     */
    List<UserCouponInfo> getAddinterestList(Map<String, Object> paramMap);

    /**
     * 根据参数获取可使用优惠劵数量
     * @param paramMap
     * @return
     */
    int getCouponsCount(Map<String, Object> paramMap);

    /**
     * 投标成功后使用返现加息卷-修改状态
     * @param paramMap
     */
    int updateUserCoupon(Map<String, Object> paramMap);

    /**
     * 获取加息券
     * @param jxqId
     * @return
     */
    UserCouponInfo getCoupon(int jxqId, int userId);

}