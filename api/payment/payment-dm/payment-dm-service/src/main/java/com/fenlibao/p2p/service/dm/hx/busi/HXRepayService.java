package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

public interface HXRepayService extends HXOrderProcess{
	/**
	 * 还款申请
	 * @param loanNo 借款编号
	 * @param term 还款期号
	 * @param remark 备注
	 * @param returnURL 返回商户URI
	 * @param clientType 客户端类型
	 * @param hxTradeType 华兴业务编码  
	 * @param userId 还款用户id  
	 * @return 返回请求报文给页面表单提交
	 * @throws Exception
	 * @throws TradeResponseCode
	 */
	String repayApply(int loanId,int term,String remark,String returnURL, APPType clientType,HXTradeType hxTradeType,int userId) throws Exception;
	/**
	 * 定时器调用，还款申请结果处理，如果申请成功将提交明细
	 * @param hxOrder
	 * @throws Exception
	 */
	RespBusinessParams queryOrder(HXOrder hxOrder) throws Exception;
	
}
