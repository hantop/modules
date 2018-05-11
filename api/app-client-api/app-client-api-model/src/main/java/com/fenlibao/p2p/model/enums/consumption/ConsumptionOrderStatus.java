package com.fenlibao.p2p.model.enums.consumption;

/**
 * 消费订单状态
 * @author zcai
 * @date 2016年4月20日
 */
public enum ConsumptionOrderStatus {

	DZF("待支付"),ZFZ("支付中"),ZFCG("支付成功"),ZFSB("支付失败"),DTK("待退款"),TKZ("退款中"),TKCG("退款成功"),TKSB("退款失败");
	
	private String name;
	
	private ConsumptionOrderStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
