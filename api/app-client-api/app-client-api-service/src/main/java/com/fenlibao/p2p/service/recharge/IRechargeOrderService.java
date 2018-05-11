package com.fenlibao.p2p.service.recharge;

import com.fenlibao.p2p.service.base.IOrderExecutor;

import java.sql.Connection;
import java.util.Map;

public interface IRechargeOrderService extends IOrderExecutor {

	void doConfirm(Connection connection, int orderId,
			Map<String, String> params) throws Throwable;
}
