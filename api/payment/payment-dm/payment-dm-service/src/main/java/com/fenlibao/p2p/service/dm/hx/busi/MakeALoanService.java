package com.fenlibao.p2p.service.dm.hx.busi;

import java.util.Map;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;

public interface MakeALoanService {
	/**
	 * 此接口只放款，如果没有人投资，请调用流标接口
	 * @param loanId 标id
	 * @param clientType 客户端类型
	 * @param pmsUserId 后台用户id
	 * @throws Exception
	 * @throws TradeResponseCode
	 * @return Map code CG成功/SB失败, msg 文字信息
	 */
	Map<String,String> makeALoanApply(int loanId,APPType clientType,Integer pmsUserId) throws Exception;
	/**
	 * 定时器调用，处理放款结果待确认的订单
	 * @param hxOrder
	 * @throws Exception
	 */
	RespBusinessParams queryOrder(HXOrder hxOrder) throws Exception;
}
