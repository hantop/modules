package com.fenlibao.p2p.model.vo.creditassignment;

import java.io.Serializable;

/**
 * 债权转让申请基础信息
 */
public class ApplyforVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private int applyforId;//申请债权转让ID
	
	private String zqTitle;//债权名称
	
	private double transferValue;//购买的价格(转出时的价格)
	
	private String zqYield;//收益率
	
	private int zqTime;//债权剩余天数(单位:天)
	
	private double discountRate;//折扣率
	
	private String[] assetTypes;//资产类型
	
	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public int getApplyforId() {
		return applyforId;
	}

	public void setApplyforId(int applyforId) {
		this.applyforId = applyforId;
	}

	public String getZqTitle() {
		return zqTitle;
	}

	public void setZqTitle(String zqTitle) {
		this.zqTitle = zqTitle;
	}

	public String getZqYield() {
		return zqYield;
	}

	public void setZqYield(String zqYield) {
		this.zqYield = zqYield;
	}

	public int getZqTime() {
		return zqTime;
	}

	public void setZqTime(int zqTime) {
		this.zqTime = zqTime;
	}

	public double getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(double transferValue) {
		this.transferValue = transferValue;
	}

	public String[] getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(String[] assetTypes) {
		this.assetTypes = assetTypes;
	}
	
}
