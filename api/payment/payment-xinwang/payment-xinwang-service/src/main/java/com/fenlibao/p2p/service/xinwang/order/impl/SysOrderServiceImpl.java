package com.fenlibao.p2p.service.xinwang.order.impl;

import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @date 2017/6/1 11:02
 */
@Service
public class SysOrderServiceImpl implements SysOrderService {
    @Resource
    private SysOrderManageDao orderManageDao;

    @Override
    public void submit(int orderId) {
        SystemOrder order = new SystemOrder();
        order.setId(orderId);
        order.setOrderStatus(XWOrderStatus.DQR);
        order.setCommitTime(new Date());
        orderManageDao.update(order);
    }

    @Override
    public void success(Integer orderId) {
        SystemOrder order = new SystemOrder();
        order.setId(orderId);
        order.setOrderStatus(XWOrderStatus.CG);
        order.setCompleteTime(new Date());
        orderManageDao.update(order);
    }

    @Override
    public void fail(Integer orderId) {
        SystemOrder order = new SystemOrder();
        order.setId(orderId);
        order.setOrderStatus(XWOrderStatus.SB);
        order.setCompleteTime(new Date());
        orderManageDao.update(order);
    }

    @Override
    public void insertOrderExceptionLog(Integer orderId, String log) {
        orderManageDao.insertOrderExceptionLog(orderId, log);
    }
}
