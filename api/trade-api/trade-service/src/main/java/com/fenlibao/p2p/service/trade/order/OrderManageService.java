package com.fenlibao.p2p.service.trade.order;

import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;

/**
 * Created by zcai on 2016/11/22.
 */
public interface OrderManageService {

    /**
     * 仅仅是将订单的状态改成 待确认
     * （如果没有其他业务处理的的可以直接提交）
     * @param orderId
     */
    void submit(int orderId);

    /**
     * 获取订单
     * @param orderId
     * @return
     */
    T6501 getOrder(int orderId);

    /**
     * 完成订单 failure or success
     * @param orderId
     * @param state
     */
    void complete(int orderId, T6501_F03 state);

}
