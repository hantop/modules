package com.fenlibao.platform.model.thirdparty.enums;

/**
 * 借款标类型： 信用认证,实地认证,抵押担保
 * @author junda.feng
 * @date 2016年6月28日
 */
public enum BidType {
	
	XYLX(1,"信用认证"),
	SDRZ(2,"实地认证"),
	DYDB(3,"抵押担保")
	;
	private int code;
	private String name;
	
	private BidType(int code,String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}
	public String getName() {
		return name;
	}

}
