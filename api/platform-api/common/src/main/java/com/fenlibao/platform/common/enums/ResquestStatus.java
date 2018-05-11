package com.fenlibao.platform.common.enums;

/**
 * 短信发送类型
 * @author yangzengcai
 * @date 2016年3月4日
 */
public enum ResquestStatus {

	STATUS_CG("CG", "成功"),
	STATUS_SB("SB", "失败"),
	STATUS_DQR("DQR", "待确认"),
	;

	private String code;

	private ResquestStatus(String code, String desc) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}
