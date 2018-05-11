package com.fenlibao.p2p.service.trade.order.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.order.MakeALoanOrderDao;
import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.model.trade.entity.T6250;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6505;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F07;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.service.trade.order.MakeALoanOrderService;

@Service
public class MakeALoanOrderServiceImpl implements MakeALoanOrderService{
	
	@Resource
	BidManageDao bidManageDao;
	@Resource
	TradeCommonDao tradeCommonDao;
	@Resource
	OrderManageDao orderManageDao;
	@Resource
	MakeALoanOrderDao makeALoanOrderDao;

	@Transactional
	@Override
	public void createOrder(Integer loanId, String channelFlow, Integer pmsUserId) throws Exception {
		Map<String,Object> params=new HashMap<>(3);
		params.put("F02", loanId);
		params.put("F07", "F");
		params.put("F08", "F");
		List<T6250> t6250List=bidManageDao.getTenderRecord(params);
		if(!t6250List.isEmpty()){
			for(T6250 t6250:t6250List){
				T6501 t6501 = new T6501();
				t6501.F02 = TradeOrderType.BID_CONFIRM.orderType();
				t6501.F03 = T6501_F03.DTJ;
				t6501.F04 = tradeCommonDao.getCurrentTimestamp();
				t6501.F07 = T6501_F07.HT;
				t6501.F08 = t6250.F03;
				t6501.F09 = pmsUserId==null?0:pmsUserId;
				t6501.F10 = channelFlow;
				orderManageDao.add(t6501);
				T6505 t6505 = new T6505();
				t6505.F01 = t6501.F01;
				t6505.F02 = t6250.F03;
				t6505.F03 = t6250.F02;
				t6505.F04 = t6250.F01;
				t6505.F05 = t6250.F04;
				makeALoanOrderDao.add(t6505);
			}
		}
		else{
			throw new TradeException(TradeResponseCode.BID_INVEST_RECORD_NOT_EXIST);
		}
	}

	@Transactional
	@Override
	public void updateOrder(T6501_F03 orderStatus, String channelFlow) throws Exception {
		T6501 t6501 = new T6501();
		t6501.F03 = orderStatus;
		t6501.F10 = channelFlow;
		orderManageDao.updateByFlowNo(t6501);
	}

}
