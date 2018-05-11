package com.fenlibao.p2p.model.mp.enums.topup;

/**
 * 手机充值面额类型
 * <p>table->mp.mp_mobile_topup_parvalue._type
 * @author yangzengcai
 * @date 2016年2月18日
 */
public enum ParvalueType {
	
	BILL(1, "话费"), TRAFFIC(2, "流量");
	
	private Integer code;
	private String desc;
	
	private ParvalueType(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
}
