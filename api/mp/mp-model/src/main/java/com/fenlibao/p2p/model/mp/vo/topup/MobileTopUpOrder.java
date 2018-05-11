package com.fenlibao.p2p.model.mp.vo.topup;

import java.math.BigDecimal;

/**
 * 手机充值订单
 * @author yangzengcai
 * @date 2016年2月17日
 */
public class MobileTopUpOrder {

	private String biztype = "mobiletopup";//固定值
	private String orderId;//订单编号
	private String mobile;//手机号码
	private String parvalue;//充值面额，为整型的字符串 比如：50，100
	private String productcode = "SHKC";//产品编码 		SHKC 移动、SHKC_CU 联通、SHKC_CT 电信
	private String extendinfo = "";//扩展信息
	private int id;//订单ID
	private BigDecimal userPayAmount;//用户需支付金额
	
	public String getBiztype() {
		return biztype;
	}
//	public void setBiztype(String biztype) {
//		this.biztype = biztype;
//	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getParvalue() {
		return parvalue;
	}
	public void setParvalue(String parvalue) {
		this.parvalue = parvalue;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getExtendinfo() {
		return extendinfo;
	}
	public void setExtendinfo(String extendinfo) {
		this.extendinfo = extendinfo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BigDecimal getUserPayAmount() {
		return userPayAmount;
	}
	public void setUserPayAmount(BigDecimal userPayAmount) {
		this.userPayAmount = userPayAmount;
	}
	
}
