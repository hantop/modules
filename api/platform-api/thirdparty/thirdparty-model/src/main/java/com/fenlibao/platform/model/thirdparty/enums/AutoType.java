package com.fenlibao.platform.model.thirdparty.enums;

/**
 * 是否自动投标
 * @author junda.feng
 * @date 2016年6月28日
 */
public enum AutoType {
	
	F("否"),
	S("是")
	;
	private String name;
	
	private AutoType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
