package com.fenlibao.p2p.dao.trade.impl;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.trade.IRechargeDao;
import com.fenlibao.p2p.model.entity.pay.RechargeOrder;
import com.fenlibao.p2p.model.vo.pay.PaymentLimitVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RechargeDaoImpl extends BaseDao implements IRechargeDao {

	public RechargeDaoImpl() {
		super("RechargeMapper");
	}
	
	@Override
	public List<RechargeOrder> getDQROrder() {
		return sqlSession.selectList(MAPPER + "getDQROrder");
	}

	@Override
	public int updateOrderStatus(Map<String, Object> params) throws Exception {
		return sqlSession.update(MAPPER + "updateStatus", params);
	}

	@Override
	public BigDecimal getOfflineRechargeAmount(int userId, int hours) {
		Map<String, Integer> params = new HashMap<>();
		params.put("userId", userId);
		params.put("hours", hours);
		return sqlSession.selectOne(MAPPER + "getOfflineRechargeAmount", params);
	}

	@Override
	public List<PaymentLimitVO> getLimitList(String bankCode, String channelCode) {
		Map<String, String> params = new HashMap<>(1);
		params.put("bankCode", bankCode);
		if (StringUtils.isNotBlank(channelCode)) {
			params.put("channelCode", channelCode);
		}
		return sqlSession.selectList(MAPPER + "getLimitList", params);
	}

	@Override
	public List<PaymentLimitVO> getCgLimit(String bankCode) {
		Map<String, String> params = new HashMap<>(2);
		params.put("bankCode", bankCode);
		return sqlSession.selectList(MAPPER + "getCgLimit", params);
	}
}
