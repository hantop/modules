package com.fenlibao.platform.common.enums;

/**
 * 短信发送类型
 * @author yangzengcai
 * @date 2016年3月4日
 */
public enum SMSType {

	REGISTER_NEW(300, "新用户注册"), 
	REGISTER_OLD(301, "老用户注册"),
	
	;
	
	private Integer code;
	
	private SMSType(Integer code, String desc) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
	
}
