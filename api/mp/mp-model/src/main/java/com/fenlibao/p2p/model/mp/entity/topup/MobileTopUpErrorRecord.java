package com.fenlibao.p2p.model.mp.entity.topup;

/**
 * 手机充值异常记录
 * @author junda.feng
 * @date 2016年11月18日
 * 
 */
public class MobileTopUpErrorRecord {
	private String customOrderCode;//订单编号
	private String state;//记录状态(WCL未处理,YCL已处理,CLSB处理失败)

	public String getCustomOrderCode() {
		return customOrderCode;
	}

	public void setCustomOrderCode(String customOrderCode) {
		this.customOrderCode = customOrderCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
