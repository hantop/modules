package com.fenlibao.p2p.service.base;

import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;

import java.sql.Connection;
import java.util.Map;

public interface IOrderExecutor {

	public void submit(int orderId, Map<String, String> params)
			throws Throwable;

	public void confirm(int orderId, Map<String, String> params)
			throws Throwable;

	public void confirm(int orderId, Map<String, String> params, OperationTypeEnum operationTypeEnum)
			throws Throwable;

	public void confirmKernel(Connection connection, int orderId, Map<String, String> params, OperationTypeEnum operationTypeEnum)
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
