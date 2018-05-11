package com.fenlibao.p2p.model.trade.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fenlibao.p2p.model.trade.enums.T6250_F07;
import com.fenlibao.p2p.model.trade.enums.T6250_F08;
import com.fenlibao.p2p.model.trade.enums.T6250_F09;

/**
 * 投标记录
 */
public class T6250 {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 标ID,参考T6230.F01
	 */
	public int F02;

	/**
	 * 投资人ID,参考T6110.F01
	 */
	public int F03;

	/**
	 * 购买价格
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 债权金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 投标时间
	 */
	public Timestamp F06;

	/**
	 * 是否取消,F:否;S:是;
	 */
	public T6250_F07 F07;

	/**
	 * 是否已放款,F:否;S:是;
	 */
	public T6250_F08 F08;

	/**
	 * 是否自动投标
	 */
	public T6250_F09 F09 = T6250_F09.F;
}