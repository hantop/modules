package com.fenlibao.p2p.service.trade.common.impl;

import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.model.trade.entity.CapitalFlow;
import com.fenlibao.p2p.model.trade.enums.T6123_F05;
import com.fenlibao.p2p.service.trade.common.TradeCommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TradeCommonServiceImpl implements TradeCommonService{

	@Resource
	TradeCommonDao tradeCommonDao;
	
	/**
	 * 发站内信
	 */
	@Override
	public void sendLetter(int userId, String title,String content) throws Exception {
		long letterId = tradeCommonDao.insertT6123(userId, title, T6123_F05.WD.name());
			if(letterId>0){
				tradeCommonDao.insertT6124(letterId, content);
			}
	}

	/**
	 * 发短信
	 */
	@Override
	public void sendMsg(String mobile, String content,int type) throws Exception {
		Integer msgId=tradeCommonDao.insertT1040(type, content);
		if(msgId!=null){
			if(msgId>0){
				tradeCommonDao.insertT1041(msgId, mobile);
			}
		}
	}

	/**
	 * 新建交易流水
	 */
	@Override
	public void addTransactionFlows(List<CapitalFlow> list) throws Exception {
		tradeCommonDao.insertT6102s(list);
	}

}
