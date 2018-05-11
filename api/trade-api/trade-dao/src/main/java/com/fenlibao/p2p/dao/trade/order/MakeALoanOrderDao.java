package com.fenlibao.p2p.dao.trade.order;

import com.fenlibao.p2p.model.trade.entity.order.T6505;
import com.fenlibao.p2p.model.trade.entity.order.T6508;

public interface MakeALoanOrderDao {
   void add(T6505 t6505) throws Exception;
   T6505 get(int id) throws Exception;
}
