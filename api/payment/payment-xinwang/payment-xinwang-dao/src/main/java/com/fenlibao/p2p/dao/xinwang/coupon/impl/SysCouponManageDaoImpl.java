package com.fenlibao.p2p.dao.xinwang.coupon.impl;

import com.fenlibao.p2p.dao.xinwang.coupon.SysCouponManageDao;
import com.fenlibao.p2p.model.xinwang.entity.coupon.XWUserCoupon;
import com.fenlibao.p2p.model.xinwang.entity.user.XWUserRedpacket;
import com.fenlibao.p2p.model.xinwang.enums.coupon.CouponState;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SysCouponManageDaoImpl implements SysCouponManageDao {
	
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "SysCouponManageMapper.";

	@Override
	public void updateRedpacket(int id, CouponState state, Integer tenderId) throws Exception {
		Map<String, Object> params = new HashMap<>(3);
		params.put("id", id);
		params.put("state", state.getCode());
		params.put("tenderId", tenderId);
		sqlSession.update(MAPPER + "updateRedpacket", params);
	}

	@Override
	public void updateUserCoupon(Integer id, CouponState state, Integer tenderId) {
		Map<String, Object> params = new HashMap<>(3);
		params.put("id", id);
		params.put("state", state.getCode());
		params.put("tenderId", tenderId);
		sqlSession.update(MAPPER + "updateUserCoupon", params);
	}

	@Override
	public XWUserCoupon getUserCouponByTenderId(Integer tenderId) {
		return sqlSession.selectOne(MAPPER + "getUserCouponByTenderId",tenderId);
	}

	@Override
	public List<XWUserRedpacket> getUserRedpacket(Integer userId, CouponState state, Integer tenderId) {
		Map<String, Object> params = new HashMap<>(5);
		params.put("userId", userId);
		if (state != null) {
			params.put("state", state.getCode());
		}
		if (tenderId != null) {
			params.put("tenderId", tenderId);
		}
		return sqlSession.selectList(MAPPER + "getUserRedpacket", params);
	}

	@Override
	public List<XWUserCoupon> getUserCounpon(Integer userId, CouponState state, Integer tenderId) {
		Map<String, Object> params = new HashMap<>(5);
		params.put("userId", userId);
		if (state != null) {
			params.put("state", state.getCode());
		}
		if (tenderId != null) {
			params.put("tenderId", tenderId);
		}
		return sqlSession.selectList(MAPPER + "getUserCounpon", params);
	}
}
