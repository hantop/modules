package com.fenlibao.p2p.dao.trade;

import java.sql.Timestamp;

public interface IOrderDao {

    /**
     * 根据订单id获取订单创建时间（t6501）
     * @param orderId
     * @return
     * @throws Exception
     */
    Timestamp getCreateTimeByOrderId(int orderId) throws Exception;
	
    
    /**
     * 根据订单ID获取用户ID
     * @param orderId
     * @return 不存在返回 null
     */
    Integer getUserIdByOrderId(int orderId);
    
	
    /**
     * 记录订单（充值、提现）与客户端类型的关系
     * @param orderId
     * @param clientType
     * @return
     */
    int insertOrderIdAndClientType(int orderId, String clientType);
}
