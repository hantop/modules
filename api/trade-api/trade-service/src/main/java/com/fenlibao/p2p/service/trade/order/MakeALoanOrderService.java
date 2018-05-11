package com.fenlibao.p2p.service.trade.order;

import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;

public interface MakeALoanOrderService {
    void createOrder(Integer loanId,String channelFlow,Integer pmsUserId) throws Exception;
    void updateOrder(T6501_F03 orderStatus,String channelFlow) throws Exception;
}
