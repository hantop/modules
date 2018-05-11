package com.fenlibao.p2p.model.mp.entity.topup;

/**
 * 手机充值中订单
 * @author junda.feng
 * @date 2016年5月19日
 * 
 */
public class MobileTopUpOrderInchargeEntity {
	private String orderId;//订单id
	private String customOrderCode;//订单编号
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCustomOrderCode() {
		return customOrderCode;
	}
	public void setCustomOrderCode(String customOrderCode) {
		this.customOrderCode = customOrderCode;
	}
	
	
}
