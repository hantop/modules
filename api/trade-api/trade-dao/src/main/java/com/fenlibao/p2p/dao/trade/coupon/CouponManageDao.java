package com.fenlibao.p2p.dao.trade.coupon;

import com.fenlibao.p2p.model.trade.entity.UserCoupon;
import com.fenlibao.p2p.model.trade.enums.coupon.UserCouponState;
import com.fenlibao.p2p.model.trade.vo.UserCouponVO;
import com.fenlibao.p2p.model.trade.vo.UserRedpacketVO;

import java.util.List;

public interface CouponManageDao {
	UserCoupon selectUserCouponByTenderId(int tenderId);
	void returnUserCoupon(int tenderId);

	/**
	 * 获取用户红包
	 * @param userId
	 * @param state
	 * @param userRedpacketIds
     * @return
     */
	List<UserRedpacketVO> getUserRedpacket(int userId, UserCouponState state, Integer bidTypeId, Integer tenderId, String... userRedpacketIds);

	/**
	 * 更新红包
	 * @param id
	 * @param state
	 * @param tenderId
	 * @throws Exception
     */
	void updateRedpacket(int id, UserCouponState state, Integer tenderId) throws Exception;

	/**
	 * 获取用户优惠券
	 * @param userId
	 * @param state
	 * @param tenderId 投标记录ID
	 * @param bidTypeId
	 * @param userCouponId
     * @return
     */
	List<UserCouponVO> getUserCoupon(int userId, UserCouponState state, Integer tenderId, Integer bidTypeId, Integer userCouponId);

	/**
	 * 更新优惠券信息
	 * @param id
	 * @param state
	 * @param tenderId
	 * @throws Exception
     */
	void updateUserCoupon(int id, UserCouponState state, Integer tenderId) throws Exception;

}
