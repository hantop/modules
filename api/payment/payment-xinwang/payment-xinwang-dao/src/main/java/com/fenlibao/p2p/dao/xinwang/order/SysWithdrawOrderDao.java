package com.fenlibao.p2p.dao.xinwang.order;

import com.fenlibao.p2p.model.xinwang.entity.order.SysWithdrawOrder;

/**
 * Created by Administrator on 2017/6/9.
 */
public interface SysWithdrawOrderDao {
    void addOrder(SysWithdrawOrder order);
    SysWithdrawOrder get(Integer orderId);
}
