package com.fenlibao.p2p.service.dm.hx.impl;

import com.fenlibao.p2p.dao.dm.hx.HXOrderDao;
import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 华兴相关的业务实现
 * Created by zcai on 2016/10/10.
 */
@Service
public class HuaXingServiceImpl implements HuaXingService {

    @Resource
    private HuaXingDao huaXingDao;
    @Resource
    private HXOrderDao hxOrderDao;

    @Override
    public void saveMessage(String tradeCode, String oldFlowNum, String flowNum, String message) {
        huaXingDao.saveMessage(tradeCode, oldFlowNum, flowNum, message);
    }

    @Override
    public HXOrder getOrder(String flowNum) {
        return huaXingDao.getOrderByFlowNum(flowNum);
    }

    @Transactional
    @Override
    public void createOrder(int clientType, HXOrder order) {
        huaXingDao.createOrder(order);
        huaXingDao.addOrderAndClientRelation(clientType, order.getId());
    }

    @Override
    public List<HXOrder> getOrders(OrderStatus state, HXTradeType tradeType) {
        return hxOrderDao.get(state, tradeType);
    }

}
