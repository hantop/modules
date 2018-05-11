package com.fenlibao.p2p.model.form.trade;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class WithdrawManageFrom {

	/**
	 * 提现总额
	 */
	public BigDecimal totalAmount=new BigDecimal(0);
	/** 
	 * 提现手续费
	 */
	public BigDecimal charge=new BigDecimal(0);
	/** 
	 * 更新时间
	 */
	public Timestamp  updateTime;
}
