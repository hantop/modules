package com.fenlibao.p2p.service.pay.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.trade.IPaymentDao;
import com.fenlibao.p2p.model.entity.pay.PaymentOrderEntity;
import com.fenlibao.p2p.model.enums.pay.PaymentOrderStatus;
import com.fenlibao.p2p.service.pay.IPaymentService;

/**
 * 支付模块
 * @author zcai
 * @date 2016年4月22日
 */
@Service
public class PaymentService implements IPaymentService {
	
	@Resource
	private IPaymentDao paymentDao;

	@Transactional
	@Override
	public PaymentOrderEntity addOrder(String amount, Integer channelCode, Integer userId) throws Exception {
		PaymentOrderEntity order = new PaymentOrderEntity();
		order.setAmount(new BigDecimal(amount));
		order.setChannelCode(channelCode);
		order.setUserId(userId);
		paymentDao.addOrder(order);
		return order;
	}

	@Transactional
	@Override
	public void updateOrder(PaymentOrderEntity order) throws Exception {
		paymentDao.updateOrder(order);
	}

	@Override
	public PaymentOrderEntity lockOrder(Integer id) throws Exception {
		return paymentDao.lockOrder(id);
	}

	@Transactional
	@Override
	public void submit(PaymentOrderEntity order) throws Exception {
		order.setSubmitTime(new Date());
		order.setStatus(PaymentOrderStatus.DQR);
		updateOrder(order);
	}

}
