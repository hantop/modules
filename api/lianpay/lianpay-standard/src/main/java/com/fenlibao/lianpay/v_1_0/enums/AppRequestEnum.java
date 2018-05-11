package com.fenlibao.lianpay.v_1_0.enums;

/**
 * 请求应用标识
 * @author zcai
 * @date 2016年4月22日
 */
public enum AppRequestEnum {

	ANDROID(1), IOS(2), WAP(3)
	
	;
	
	private Integer code;
	
	private AppRequestEnum(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
	
}
