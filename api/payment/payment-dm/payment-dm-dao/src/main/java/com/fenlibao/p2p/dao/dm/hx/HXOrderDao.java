package com.fenlibao.p2p.dao.dm.hx;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;

import java.util.List;

/**
 * Created by zcai on 2016/12/24.
 */
public interface HXOrderDao {

    /**
     * 获取指定业务指定状态订单
     * @param state
     * @param tradeType
     * @return
     */
    List<HXOrder> get(OrderStatus state, HXTradeType tradeType);

}
