package com.fenlibao.p2p.model.dm.entity;

import java.io.Serializable;

/**
 * 华兴还款订单
 * @author Administrator
 *
 */
public class HXRepayOrder implements Serializable{
	private int orderId;
	private int term;
	private int dfflag;

	public HXRepayOrder() {
	}
	
	/**
	 * 
	 * @param orderId 华兴订单id
	 * @param term 还款期号
	 * @param dfflag 1=正常还款  2=垫付后，借款人还款

	 */
	public HXRepayOrder(int orderId, int term, int dfflag) {
		this.orderId = orderId;
		this.term = term;
		this.dfflag = dfflag;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public int getDfflag() {
		return dfflag;
	}

	public void setDfflag(int dfflag) {
		this.dfflag = dfflag;
	}
}
