package com.fenlibao.p2p.model.trade.enums.common;

/**
 * 短信发送类型 1040.F02
 * 
 */
public enum MsgSendType {
	BD(1), ZD(2);

	protected final int value;

	private MsgSendType(int value) {
		this.value = value;
	}

	/**
	 * 获取来源编码.
	 * 
	 * @return {@link String}
	 */
	public int getValue() {
		return value;
	}
}
