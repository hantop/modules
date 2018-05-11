package com.fenlibao.p2p.service.recharge;

import java.sql.Connection;
import java.util.Map;

import com.fenlibao.p2p.service.base.IOrderExecutor;

public interface IRechargeOrderService extends IOrderExecutor {

	public void doConfirm(Connection connection, int orderId,
			Map<String, String> params) throws Throwable;
}
