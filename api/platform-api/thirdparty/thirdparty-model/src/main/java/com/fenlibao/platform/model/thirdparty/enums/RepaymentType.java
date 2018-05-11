package com.fenlibao.platform.model.thirdparty.enums;

/**网贷之家
 * 还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金
 * @author junda.feng
 * @date 2016年6月28日
 */
public enum RepaymentType {
	
	DEBX(2,"等额本息"),
	MYFX(5,"每月付息,到期还本"),
	YCFQ(1,"本息到期一次付清"),
	DEBJ(6,"等额本金")
	;
	private String name;
	private int code;
	
	private RepaymentType(int code,String name) {
		this.code =code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}
	public String getName() {
		return name;
	}

}
