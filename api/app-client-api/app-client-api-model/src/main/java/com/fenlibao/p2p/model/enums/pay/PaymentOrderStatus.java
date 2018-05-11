package com.fenlibao.p2p.model.enums.pay;

/**
 * 支付订单状态
 * @author zcai
 * @date 2016年4月20日
 */
public enum PaymentOrderStatus {
	
	DTJ("待提交"),
	YTJ("已提交"),
	DQR("待确认"),
	CG("成功"),
	SB("失败"),
	MJL("没记录"), //在第三方没有记录
	;
	private String name;
	
	private PaymentOrderStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
