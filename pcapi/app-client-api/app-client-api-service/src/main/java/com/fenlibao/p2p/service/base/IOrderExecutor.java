package com.fenlibao.p2p.service.base;

import java.util.Map;

public interface IOrderExecutor {

	public void submit(int orderId, Map<String, String> params)
			throws Throwable;
	
	public void confirm(int orderId, Map<String, String> params)
			throws Throwable;
	
	/**
	 * 提现用，主要对sqlException不进行处理
	 * @param orderId
	 * @param params
	 * @throws Throwable
	 */
	void confirmForPay(int orderId, Map<String, String> params)
			throws Throwable;
	
}
