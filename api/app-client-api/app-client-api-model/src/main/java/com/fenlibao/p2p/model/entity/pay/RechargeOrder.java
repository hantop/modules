package com.fenlibao.p2p.model.entity.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S65.enums.T6501_F03;

public class RechargeOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2989258678905744477L;

	/**
	 * 订单id
	 */
	public int id;
	/**
	 * 下单时间(T6501)
	 */
	public Timestamp orderTime;
	/**
	 * 充值金额
	 */
	public BigDecimal amount;
	/**
	 * 支付公司代码
	 */
	public int payCompanyCode;
	/**
	 * 支付状态
	 */
	public T6501_F03 status;
	/**
	 * added by zcai 20150824
	 * 用户ID,参考T6110.F01
	 */
	public int userId;
	
}
