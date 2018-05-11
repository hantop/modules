package com.fenlibao.p2p.service.trade.order.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.trade.order.RepayOrderDao;
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.entity.order.T6501;
import com.fenlibao.p2p.model.trade.entity.order.T6506;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F07;
import com.fenlibao.p2p.model.trade.enums.order.TradeOrderType;
import com.fenlibao.p2p.service.trade.order.RepayOrderService;

@Service
public class RepayOrderServiceImpl implements RepayOrderService{

	@Resource
	BidManageDao bidManageDao;
	
	@Resource
	OrderManageDao orderManageDao;
	
	@Resource
	TradeCommonDao tradeCommonDao;
	
	@Resource
	RepayOrderDao repayOrderDao;
	
	@Transactional
	@Override
	public List<T6252> createOrder(int loanId,int term, String channelFlow, Integer pmsUserId) throws Exception {
		Map<String,Object> p1=new HashMap<>(4);
		p1.put("F02", loanId);
		List<Integer> F05List=new ArrayList<>();
		F05List.add(TradeFeeCode.TZ_BJ);
		F05List.add(TradeFeeCode.TZ_LX);
		F05List.add(TradeFeeCode.TZ_FX);
		F05List.add(TradeFeeCode.TZ_YQ_SXF);
		p1.put("F05List", F05List);
		p1.put("F06", term);
		p1.put("F09", "WH");
		List<T6252> t6252List=bidManageDao.getRepayPlan(p1);
		if(t6252List!=null){
			for(T6252 t6252:t6252List){
				T6501 t6501 = new T6501();
				t6501.F02 = TradeOrderType.BID_REPAYMENT.orderType();
				t6501.F03 = T6501_F03.DTJ;
				t6501.F04 = tradeCommonDao.getCurrentTimestamp();
				t6501.F07 = T6501_F07.YH;
				t6501.F08 = t6252.F03;
				t6501.F09 = pmsUserId==null?0:pmsUserId;
				t6501.F10 = channelFlow;
				orderManageDao.add(t6501);
				T6506 t6506 = new T6506();
				t6506.F01 = t6501.F01;
				t6506.F02 = t6252.F04;
				t6506.F03 = t6252.F02;
				t6506.F04 = t6252.F11;
				t6506.F05 = t6252.F06;
				t6506.F06 = t6252.F07;
				t6506.F07 = t6252.F05;
				repayOrderDao.add(t6506);
			}
		}
		return t6252List;
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
