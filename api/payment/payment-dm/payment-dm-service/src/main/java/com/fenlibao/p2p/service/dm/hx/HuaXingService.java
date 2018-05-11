package com.fenlibao.p2p.service.dm.hx;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;

import java.util.List;

/**
 * Created by zcai on 2016/10/10.
 */
public interface HuaXingService {

    /**
     * 保存华兴回调报文
     * @param tradeCode
     * @param flowNum
     * @param message
     */
    void saveMessage(String tradeCode, String oldFlowNum, String flowNum, String message);

    /**
     * 获取订单并锁定
     * @param flowNum
     * @return
     */
    HXOrder getOrder(String flowNum);

    /**
     * 创建订单
     * @param clientType
     * @param order
     */
    void createOrder(int clientType, HXOrder order);


    /**
     * 获取指定业务指定状态订单
     * @param state
     * @param tradeType
     * @return
     */
    List<HXOrder> getOrders(OrderStatus state, HXTradeType tradeType);

}
