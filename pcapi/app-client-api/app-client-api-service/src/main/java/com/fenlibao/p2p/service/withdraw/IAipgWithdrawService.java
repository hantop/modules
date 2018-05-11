package com.fenlibao.p2p.service.withdraw;

import com.fenlibao.p2p.service.base.IOrderExecutor;

public interface IAipgWithdrawService extends IOrderExecutor {

	void insertRefundFailLog(int orderId, String log) throws Exception;
	
}
