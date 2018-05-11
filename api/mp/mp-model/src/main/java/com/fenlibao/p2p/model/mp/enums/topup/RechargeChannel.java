package com.fenlibao.p2p.model.mp.enums.topup;

/**
 * 话费充值渠道
 *
 */
public enum RechargeChannel {

	JFYP(0, "劲峰优品"),
	YS(1, "易赏");//fengjunda 2016-4-25
	
	private Integer code;
	private String desc;
	
	private RechargeChannel(Integer code, String desc) {
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
