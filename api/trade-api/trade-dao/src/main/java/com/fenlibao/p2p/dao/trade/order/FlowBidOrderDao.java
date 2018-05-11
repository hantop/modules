package com.fenlibao.p2p.dao.trade.order;

import com.fenlibao.p2p.model.trade.entity.order.T6508;

public interface FlowBidOrderDao {
   void add(T6508 t6508) throws Exception;
   T6508 get(int id) throws Exception;
}
