/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: ITenderExchangeOrderService.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 下午3:51:18 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.service.creditassignment;

import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;
import com.fenlibao.p2p.service.base.IOrderExecutor;

import java.sql.Connection;
import java.util.Map;

/** 
 * @ClassName: ITenderExchangeOrderService 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-23 下午3:51:18  
 */
public interface TenderExchangeOrderService extends IOrderExecutor{
	
	void doConfirm(Connection connection, int orderId, Map<String, String> params, OperationTypeEnum operationTypeEnum) throws Throwable;
	
}
