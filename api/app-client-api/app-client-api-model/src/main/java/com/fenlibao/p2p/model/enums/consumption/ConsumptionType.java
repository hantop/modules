package com.fenlibao.p2p.model.enums.consumption;

/**
 * 消费类型(table > flb.consumption_type)
 * @author zcai
 * @date 2016年4月21日
 */
public enum ConsumptionType {

	/**
	 * 手机充值
	 */
	SJCZ(101, "手机充值")
	
	;
	
	private Integer code;
	private String name;
	
	private ConsumptionType(Integer code, String name) {
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
