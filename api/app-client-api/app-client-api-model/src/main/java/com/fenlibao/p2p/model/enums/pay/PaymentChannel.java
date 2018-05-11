package com.fenlibao.p2p.model.enums.pay;

/**
 * 支付通道
 * @author zcai
 * @date 2016年4月20日
 */
public enum PaymentChannel {

	LLP_QUICK(101, "连连快捷支付")
	
	;
	
	private Integer code;
	private String name;
	
	private PaymentChannel(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
}
