package com.fenlibao.p2p.service.trade.order.impl;

import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.service.trade.order.OrderManageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * Created by zcai on 2016/11/22.
 */
@Service
public class OrderManageServiceImpl implements OrderManageService {

    @Resource
    private OrderManageDao orderManageDao;

    @Override
    public void submit(int orderId) {
        T6501 order = new T6501();
        order.F01 = orderId;
        order.F03 = T6501_F03.DQR;
        order.F05 = new Timestamp(System.currentTimeMillis());
        orderManageDao.update(order);
    }

    @Override
    public T6501 getOrder(int orderId) {
        return orderManageDao.get(orderId);
    }

    @Override
    public void complete(int orderId, T6501_F03 state) {
        T6501 order = new T6501();
        order.F01 = orderId;
        order.F03 = state;
        order.F06 = new Timestamp(System.currentTimeMillis());
        orderManageDao.update(order);
    }
}
