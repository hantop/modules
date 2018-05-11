package com.fenlibao.p2p.model.mp.enums.topup;

/**
 * 充值状态
 * <p>table->mp.mp_mobile_topup_order._status
 * @author yangzengcai
 * @date 2016年2月18日
 */
public enum TopUpStatus {

	WAIT(0, "待提交"), ING(1, "充值中"), SUCCESS(2, "充值成功"), FAILURE(3, "充值失败");
	
	private Integer code;
	private String desc;
	
	private TopUpStatus(Integer code, String desc) {
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
