package com.fenlibao.p2p.model.trade.enums;

/**
 * 标来源
 * 
 */
public enum BidSource {
	FLB("0001"), QQM("0002");

	protected final String code;

	private BidSource(String code) {
		this.code = code;
	}

	/**
	 * 获取来源编码.
	 * 
	 * @return {@link String}
	 */
	public String getCode() {
		return code;
	}
}
