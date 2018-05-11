package com.fenlibao.p2p.service.dm.hx.busi;

import java.math.BigDecimal;
import java.util.Map;

import com.fenlibao.p2p.model.dm.entity.HXOrder;

public interface RepayAdvanceService {
	/**
	 * 还款垫付
	 * @param loanId 标id
	 * @param feeAmt 扣借款人的平台手续费
	 * @param remark 备注
	 * @param pmsUserId 后台用户id
	 * @throws Exception
	 * @throws TradeException
	 * @return Map code CG成功/SB失败, msg 文字信息
	 */
	Map<String,String> repayAdvance(int loanId,BigDecimal feeAmt,String remark,Integer pmsUserId) throws Exception;
	/**
	 * 定时器调用，用于在华兴同步操作成功但我方业务代码报错回滚的时候再次运行我方代码
	 * @param order
	 * @throws Exception
	 * @throws TradeException
	 */
	void reProcess(HXOrder order) throws Exception;
}
