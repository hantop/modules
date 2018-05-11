package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

public interface RepayDetailSubmitService extends HXOrderProcess{
	/**
	 * 提交还款明细，如果不是明细提交失败不需要手动调用
	 * @param hxOrder
	 * @throws Exception
	 */
	void repayDetailSubmit(HXOrder hxOrder) throws Exception;
	/**
	 * 定时器调用，还款明细提交结果处理
	 * @param hxOrder
	 * @throws Exception
	 */
	RespBusinessParams queryOrder(HXOrder hxOrder) throws Exception;
}
