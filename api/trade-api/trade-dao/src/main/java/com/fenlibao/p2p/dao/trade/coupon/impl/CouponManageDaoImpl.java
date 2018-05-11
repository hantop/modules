package com.fenlibao.p2p.dao.trade.coupon.impl;

import com.fenlibao.p2p.dao.trade.coupon.CouponManageDao;
import com.fenlibao.p2p.model.trade.entity.UserCoupon;
import com.fenlibao.p2p.model.trade.enums.coupon.UserCouponState;
import com.fenlibao.p2p.model.trade.vo.UserCouponVO;
import com.fenlibao.p2p.model.trade.vo.UserRedpacketVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CouponManageDaoImpl implements CouponManageDao{
	
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "CouponManageMapper.";

	@Override
	public UserCoupon selectUserCouponByTenderId(int tenderId) {
		UserCoupon userCoupon=sqlSession.selectOne(MAPPER+"selectUserCouponByTenderId", tenderId);
		return userCoupon;
	}

	@Override
	public void returnUserCoupon(int tenderId) {
		Map<String,Object> params=new HashMap<>(1);
		params.put("tenderId", tenderId);
		sqlSession.update(MAPPER+"returnUserCoupon", params);
	}

	@Override
	public List<UserRedpacketVO> getUserRedpacket(int userId, UserCouponState state, Integer bidTypeId, Integer tenderId, String... userRedpacketIds) {
		Map<String, Object> params = new HashMap<>(5);
		params.put("userId", userId);
		if (state != null) {
			params.put("state", state.getCode());
		}
		if (bidTypeId != null) {
			params.put("bidTypeId", bidTypeId);
		}
		if (tenderId != null) {
			params.put("tenderId", tenderId);
		}
		if (userRedpacketIds != null) {
			params.put("userRedpacketIds", userRedpacketIds);
		}
		return sqlSession.selectList(MAPPER + "getUserRedpacket", params);
	}

	@Override
	public void updateRedpacket(int id, UserCouponState state, Integer tenderId) throws Exception {
		Map<String, Object> params = new HashMap<>(3);
		params.put("id", id);
		params.put("state", state.getCode());
		params.put("tenderId", tenderId);
		sqlSession.update(MAPPER + "updateRedpacket", params);
	}

	@Override
	public List<UserCouponVO> getUserCoupon(int userId, UserCouponState state, Integer tenderId, Integer bidTypeId, Integer userCouponId) {
		Map<String, Integer> params = new HashMap<>(5);
		params.put("userId", userId);
		if (state != null) {
			params.put("state", state.getCode());
		}
		if (bidTypeId != null) {
			params.put("bidTypeId", bidTypeId);
		}
		if (tenderId != null) {
			params.put("tenderId", tenderId);
		}
		if (userCouponId != null) {
			params.put("userCouponId", userCouponId);
		}
		return sqlSession.selectList(MAPPER + "getUserCoupon", params);
	}

	@Override
	public void updateUserCoupon(int id, UserCouponState state, Integer tenderId) throws Exception {
		Map<String, Object> params = new HashMap<>(3);
		params.put("id", id);
		params.put("state", state.getCode());
		params.put("tenderId", tenderId);
		sqlSession.update(MAPPER + "updateUserCoupon", params);
	}

}
