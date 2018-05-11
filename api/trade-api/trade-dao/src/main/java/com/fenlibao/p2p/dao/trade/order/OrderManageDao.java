package com.fenlibao.p2p.dao.trade.order;

import java.util.List;

import com.fenlibao.p2p.model.trade.entity.bid.T6504;
import com.fenlibao.p2p.model.trade.entity.order.T6501;

/**
 * 为了避免重复加个manage
 * Created by zcai on 2016/11/1.
 */
public interface OrderManageDao {

    /**
     * 添加订单
     * @param order
     */
    void add(T6501 order);

    /**
     * 更新订单
     * @param order
     */
    void update(T6501 order);
    
    /**
     * 根据流水号更新订单
     * @param order
     */
    void updateByFlowNo(T6501 order);

    /**
     * 获取订单
     * @param id
     */
    T6501 get(int id);
    
    /**
     * 获取订单
     * @param id
     */
    List<T6501> getByFlowNo(String code);

    /**
     * 添加投标订单
     * @param tenderOrder
     */
    void addTenderOrder(T6504 tenderOrder);

    T6501 getBySerialNum(String serialNum);
}
