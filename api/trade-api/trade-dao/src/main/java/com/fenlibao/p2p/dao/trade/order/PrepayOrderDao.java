package com.fenlibao.p2p.dao.trade.order;

import com.fenlibao.p2p.model.trade.entity.order.T6521;

public interface PrepayOrderDao {
   void add(T6521 t6521) throws Exception;
   T6521 get(int id) throws Exception;
}
