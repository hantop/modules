package com.fenlibao.p2p.service.dm.hx.busi.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.service.dm.hx.busi.BankActiveFlowBidService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.order.FlowBidOrderService;

@Service
public class BankActiveFlowBidServiceImpl extends HXOrderProcessImpl implements BankActiveFlowBidService{
	
	@Resource
	FlowBidOrderService flowBidOrderService;
	
	@Resource
	BidManageDao bidManageDao;
	
	@Resource
	BidManageService bidManageService;
	
    @Transactional
    @Override
    public void process(RespBusinessParams busiParams, String thirdPartyFlowNum) throws Exception {
    	String loanCode=busiParams.getLOANNO();
    	T6230 t6230= bidManageDao.getBidByCode(loanCode);
    	flowBidOrderService.createOrder(t6230.F01,thirdPartyFlowNum, null);
    	flowBidOrderService.updateOrder(T6501_F03.DQR,thirdPartyFlowNum);
    	try{
    	    bidManageService.flowBid(t6230.F01, thirdPartyFlowNum);
    	}
    	catch(Exception e){
    		flowBidOrderService.updateOrder(T6501_F03.SB, thirdPartyFlowNum);
    		logger.error("标"+t6230.F01+"银行主动流标失败", e);
    		throw e;
    	}
    	flowBidOrderService.updateOrder(T6501_F03.CG, thirdPartyFlowNum);
    	logger.info("标"+t6230.F01+"银行主动流标成功");
    }
}
