package com.fenlibao.p2p.dao.coupon;

import com.fenlibao.p2p.model.entity.coupons.RateCoupon;
import com.fenlibao.p2p.model.entity.coupons.UserCouponInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by junda.feng 20160819
 */
public interface CouponDao {
    /**
     * 增加用户加息券
     *
     * @param userCouponInfos
     */
    void addUserRateCoupons(List<UserCouponInfo> userCouponInfos);

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
     * 投资计划成功后使用返现加息卷-修改状态
     * @param paramMap
     */
    int updateUserCouponForPlan(Map<String, Object> paramMap);

    /**
     * 投资计划成功后使用返现加息卷
     * @param paramMap
     */
    int insertUserCouponExt(Map<String, Object> paramMap);

    /**
     * 投标的时候检验加息券能否使用
     * @param paramMap
     */
    int checkCouponBeforeInvest(Map<String, Object> paramMap);
    /**
     * 获取活动相对应的优惠券
     * @param activityCode
     * @return
     */
    List<UserCouponInfo> getByActivityCode(String activityCode);

    /**
     * 发放加息券
     * @param couponInfo
     */
    void grantRateCoupon(UserCouponInfo couponInfo);

    /**
     *  获取固定设置相对应的加息券
     * @param param
     * @return
     */
    List<UserCouponInfo> getByTenderSetting(Map param);

    /**
     * 获取加息券
     * @param jxqId
     * @return
     */
    UserCouponInfo getCoupon(int jxqId, int userId);
}
