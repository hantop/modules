package com.fenlibao.p2p.dao.xinwang.coupon;

import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.user.XWUserRedpacket;
import com.fenlibao.p2p.model.xinwang.enums.coupon.CouponState;

import java.util.List;

public interface SysCouponManageDao {

	/**
	 * 更新红包
	 * @param id
	 * @param state
	 * @param tenderId
	 * @throws Exception
     */
	void updateRedpacket(int id, CouponState state, Integer tenderId) throws Exception;

	/**
	 * 更新加息券
	 * @param id
	 * @param state
	 * @param tenderId
	 */
	void updateUserCoupon(Integer id, CouponState state, Integer tenderId);

	/**
	 * 获取用户使用的加息券
	 * @param tenderId
	 * @return
     */
	XWUserCoupon getUserCouponByTenderId(Integer tenderId);

	/**
	 * 获取用户红包
	 * @param userId
	 * @param state
	 * @param tenderId
	 * @return
	 */
    List<XWUserRedpacket> getUserRedpacket(Integer userId, CouponState state, Integer tenderId);

	/**
	 * 获取用户的加息券列表
	 * @param userId
	 * @param state
	 * @param tenderId
	 * @return
	 */
	List<XWUserCoupon> getUserCounpon(Integer userId, CouponState state, Integer tenderId);
}
