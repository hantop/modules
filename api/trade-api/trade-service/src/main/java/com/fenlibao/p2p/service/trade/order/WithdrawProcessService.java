package com.fenlibao.p2p.service.trade.order;

import java.util.List;

import com.fenlibao.p2p.model.trade.entity.order.T6503;

/**
 * Created by zcai on 2016/11/28.
 */
public interface WithdrawProcessService {

    /**
     * 添加提现订单
     * @param withdrawOrder
     * @return 提现订单ID
     * @throws Exception
     */
    int addOrder(T6503 withdrawOrder,String flowNum) throws Exception;
    
    T6503 getOrder(int orderId) throws Exception;
    
    void updateOrder(T6503 withdrawOrder) throws Exception;
    /**
     * 获取待确认的订单
     * @param paymentInstitutionCode
     * @return
     */
    List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode) throws Exception;
}
