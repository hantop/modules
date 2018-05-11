package com.fenlibao.p2p.dao;

import java.util.Map;

public interface BidRefundDao {

	/**
	 * 获取用户收益
	 * @param map
	 * @return
	 */
	public double getPredictEarnings(Map<String, Object> map);
}
