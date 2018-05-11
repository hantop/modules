package com.fenlibao.p2p.model.entity.creditassignment;

import java.math.BigDecimal;

public interface AddTransfer {

	/**
	 * 债权ID
	 * 
	 * @return {@link int}
	 */
	public abstract int getTransferId();
	/**
	 * 转让债权
	 * @return
	 */
	public abstract BigDecimal getBidValue();
	/**
	 * 转出价格
	 * @return
	 */
	public abstract BigDecimal getTransferValue();
	/**
	 * 转让手续费
	 * @return
	 */
	public abstract BigDecimal getRateMoney();
	
	/**
	 * 债权已过天数的收益
	 * @return
	 */
	public abstract BigDecimal getPassedEarning();
}
