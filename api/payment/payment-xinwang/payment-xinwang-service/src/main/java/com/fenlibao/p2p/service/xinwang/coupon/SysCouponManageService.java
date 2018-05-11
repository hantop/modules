package com.fenlibao.p2p.service.xinwang.coupon;


import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.user.XWUserRedpacket;
import com.fenlibao.p2p.model.xinwang.enums.coupon.CouponState;

import java.util.List;

/**
 * Created by zcai on 2016/12/19.
 */
public interface SysCouponManageService {

    /**
     * 更新红包
     * @param ids
     * @param state
     * @param tenderId
     * @throws Exception
     */
    void updateRedpacket(CouponState state, Integer tenderId, String... ids) throws Exception;

    /**
     * 更新加息券
     * @param jxqId
     * @param lock
     * @param tenderId
     */
    void updateUserCoupon(Integer jxqId, CouponState lock, int tenderId);

    /**
     * 获取用户的红包
     * @param userId
     * @param state
     * @param tenderId
     * @return
     */
    List<XWUserRedpacket> getUserRedpacket(int userId, CouponState state, Integer tenderId);

    /**
     * 获取用户的加息券
     * @param tenderId
     * @return
     */
    XWUserCoupon getUserCouponByTenderId(int tenderId);

    /**
     * 获取用户的加息券列表
     * @param userId
     * @param state
     * @param tenderId
     * @return
     */
    List<XWUserCoupon> getUserCoupon(int userId, CouponState state, int tenderId);
}
