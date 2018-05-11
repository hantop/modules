package com.fenlibao.p2p.model.trade.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.fenlibao.p2p.model.trade.enums.T6231_F19;
import com.fenlibao.p2p.model.trade.enums.T6231_F21;

/**
 * 标扩展信息
 */
public class T6231 {

	private static final long serialVersionUID = 1L;

	/**
	 * 标ID,参考T6230.F01
	 */
	public int F01;

	/**
	 * 还款总期数
	 */
	public int F02;

	/**
	 * 剩余期数
	 */
	public int F03;

	/**
	 * 月化收益率
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 日化收益率
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 下次还款日期
	 */
	public Date F06;

	/**
	 * 项目区域位置ID,参考T5119.F01
	 */
	public int F07;

	/**
	 * 资金用途
	 */
	public String F08;

	/**
	 * 借款描述
	 */
	public String F09;

	/**
	 * 审核时间
	 */
	public Timestamp F10;

	/**
	 * 满标时间
	 */
	public Timestamp F11;

	/**
	 * 放款时间
	 */
	public Timestamp F12;

	/**
	 * 结清时间
	 */
	public Timestamp F13;

	/**
	 * 垫付时间
	 */
	public Timestamp F14;

	/**
	 * 流标时间
	 */
	public Timestamp F15;

	/**
	 * 还款来源
	 */
	public String F16;

	/**
	 * 起息日期
	 */
	public Date F17;

	/**
	 * 结束日期
	 */
	public Date F18;

	/**
	 * 是否逾期,S:是(逾期);F:否;YZYQ:严重逾期
	 */
	public T6231_F19 F19;
	/**
	 * 还款提前预警日
	 */
	public int F20;
	/**
	 * 是否为按天借款,S:是;F:否
	 */
	public T6231_F21 F21;
	/**
	 * 借款天数
	 */
	public int F22;

}