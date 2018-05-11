package com.fenlibao.p2p.service.trade.order.impl;

import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.trade.order.WithdrawProcessDao;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6503;
import com.fenlibao.p2p.model.trade.enums.PaymentInstitution;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F07;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.service.trade.order.WithdrawProcessService;
import com.fenlibao.p2p.util.trade.order.OrderUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by zcai on 2016/11/28.
 */
@Service
public class WithdrawProcessServiceImpl implements WithdrawProcessService {

	@Resource
	private OrderManageDao orderManageDao;
	@Resource
	private WithdrawProcessDao withdrawProcessDao;

	@Transactional
	@Override
	public int addOrder(T6503 withdrawOrder,String flowNum) throws Exception {
		T6501 order = new T6501();
		order.F02 = TradeOrderType.WITHDRAW.orderType();
		order.F03 = T6501_F03.DTJ;
		order.F07 = T6501_F07.YH;
		order.F08 = withdrawOrder.F02;
        order.F10 = flowNum; //系统流水号（非第三方）
		orderManageDao.add(order);

		// 添加提现订单
		withdrawOrder.F01 = order.F01;
		withdrawProcessDao.addOrder(withdrawOrder);
		return order.F01;
	}

	@Override
	public T6503 getOrder(int orderId) throws Exception {
		return withdrawProcessDao.getOrder(orderId);
	}

	@Override
	public void updateOrder(T6503 withdrawOrder) throws Exception {
		withdrawProcessDao.updateOrder(withdrawOrder);
	}

	@Override
	public List<Integer> getOrderNeedConfirmed(int paymentInstitutionCode) throws Exception {
		return withdrawProcessDao.getOrderNeedConfirmed(paymentInstitutionCode);
	}
}
