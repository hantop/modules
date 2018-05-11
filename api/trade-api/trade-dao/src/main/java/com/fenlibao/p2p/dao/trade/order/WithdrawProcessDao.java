package com.fenlibao.p2p.dao.trade.order;

import java.util.List;

import com.fenlibao.p2p.model.trade.entity.order.T6503;

/**
 * Created by zcai on 2016/11/29.
 */
public interface WithdrawProcessDao {

    /**
     * 添加提现订单
     * @param order
     * @throws Exception
     */
    void addOrder(T6503 order) throws Exception;

    /**
     * 获取提下订单
     * @param orderId
     * @return
     */
    T6503 getOrder(int orderId);

    void updateOrder(T6503 order);
    /**
     * 获取待确认的订单
     * @param paymentInstitutionCode
     * @return
     */
    List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode);
}
