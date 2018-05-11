package com.fenlibao.p2p.model.xinwang.enums.common;

/**
 * 短信发送类型 1040.F02
 * 
 */
public enum SysMsgSendType {
	PASSIVE(1), ACTIVE(2);

	protected final int value;

	private SysMsgSendType(int value) {
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
