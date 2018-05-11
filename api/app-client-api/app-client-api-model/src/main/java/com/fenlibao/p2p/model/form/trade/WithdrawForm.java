package com.fenlibao.p2p.model.form.trade;

import java.sql.Timestamp;

/**
 * 提现form
 * @author yangzengcai
 * @date 2015年9月8日
 */
public class WithdrawForm {

	/**
	 * 订单ID
	 */
	private int orderId;
	/**
	 * 下单时间(T6501)
	 */
	private Timestamp orderTime;

	public int getOrderId() {
		return orderId;
	}
	

	public WithdrawForm() {
		super();
	}


	public WithdrawForm(int orderId, Timestamp orderTime) {
		super();
		this.orderId = orderId;
		this.orderTime = orderTime;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	
	
}
