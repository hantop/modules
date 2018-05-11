package com.fenlibao.p2p.model.enums.pay;

/**
 * 退款状态
 * @author zcai
 * @date 2016年4月27日
 */
public enum RefundStatus {

	DTK("待退款"),TKZ("退款中"),CG("退款成功"),SB("退款失败");
	
	private String name;
	
	private RefundStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
