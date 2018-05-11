package com.fenlibao.p2p.model.mp.vo.topup;

/**
 * 充值（话费、流量）面额
 * @author yangzengcai
 * @date 2016年2月18日
 */
public class ParvalueVO {

	private Integer code;//面额编码
	private String parvalue;
	private String sellingPrice;
	private String integralCode;//可以使用积分类型
	private int integralQty;//积分
	private int sellingFlag;//0已兑完1可以兑换

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getParvalue() {
		return parvalue;
	}
	public void setParvalue(String parvalue) {
		this.parvalue = parvalue;
	}
	public String getIntegralCode() {
		return integralCode;
	}
	public void setIntegralCode(String integralCode) {
		this.integralCode = integralCode;
	}
	public String getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getIntegralQty() {
		return integralQty;
	}

	public void setIntegralQty(int integralQty) {
		this.integralQty = integralQty;
	}

	public int getSellingFlag() {
		return sellingFlag;
	}

	public void setSellingFlag(int sellingFlag) {
		this.sellingFlag = sellingFlag;
	}
}
