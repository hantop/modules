package com.fenlibao.p2p.model.mp.enums;

/**
 * 
 * @author yangzengcai
 * @date 2016年2月18日
 */
public enum SysBoolean {

	TRUE(1, true, "是"), FALSE(0, false, "否");
	
	private Integer code;
	private boolean value;
	private String desc;
	
	private SysBoolean(Integer code, boolean value, String desc) {
		this.code = code;
		this.value = value;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public boolean isValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
}
