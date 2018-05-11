package com.fenlibao.p2p.model.entity.pay;

import java.io.Serializable;

/**
 * 支行信息
 * @author yangzengcai
 * @date 2015年9月16日
 */
public class BranchInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2151463855334082223L;

	/**
	 * 提现申请订单ID（T6130）
	 */
	private Integer orderId;
	/**
	 * 支行所在城市编码
	 */
	private String cityCode;
	/**
	 * 支行名称
	 */
	private String branchName;
	
	public BranchInfo(Integer orderId, String cityCode, String branchName) {
		super();
		this.orderId = orderId;
		this.cityCode = cityCode;
		this.branchName = branchName;
	}
	public BranchInfo() {
		super();
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	
	
}
