package com.fenlibao.p2p.service.trade.order;

import java.util.List;

import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;

public interface PrepayOrderService {
	List<T6252> createOrder(int loanId,int term,String channelFlow,Integer pmsUserId) throws Exception;
    void updateOrder(T6501_F03 orderStatus,String channelFlow) throws Exception;
}
