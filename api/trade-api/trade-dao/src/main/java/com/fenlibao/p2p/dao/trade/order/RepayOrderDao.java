package com.fenlibao.p2p.dao.trade.order;

import com.fenlibao.p2p.model.trade.entity.order.T6506;

public interface RepayOrderDao {
   void add(T6506 t6506) throws Exception;
   T6506 get(int id) throws Exception;
}
