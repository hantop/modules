package com.fenlibao.p2p.dao.xinwang.order;

import com.fenlibao.p2p.model.xinwang.entity.order.BidOrder;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;

import java.util.List;

/**
 * 为了避免重复加个manage
 * Created by zcai on 2016/11/1.
 */
public interface SysOrderManageDao {

    /**
     * 添加订单
     * @param order
     */
    void add(SystemOrder order);

    /**
     * 更新订单
     * @param order
     */
    void update(SystemOrder order);
    
    /**
     * 根据流水号更新订单
     * @param order
     */
    void updateByFlowNo(SystemOrder order);

    /**
     * 获取订单
     * @param id
     * @param lock 是否锁数据
     */
    SystemOrder get(int id,boolean lock);
    
    /**
     * 获取订单
     * @param code
     */
    List<SystemOrder> getByFlowNo(String code);

    /**
     * 添加投标订单
     * @param tenderOrder
     */
    void addTenderOrder(BidOrder tenderOrder);

    SystemOrder getBySerialNum(String serialNum);

    /**
     * 记录异常订单
     * @param orderId
     * @param log
     */
    void insertOrderExceptionLog(Integer orderId, String log);
}
