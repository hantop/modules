package com.fenlibao.p2p.model.vo.creditassignment;

import java.io.Serializable;

/**
 * 债权转让申请详情
 */
public class ApplyforDetailVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int applyforId;//申请债权转让ID
	
	private String zqTitle;//债权名称
	
	private double transferValue;//购买的价格(转出时的价格)
	
	private String zqYield;//收益率
	
	private int zqTime;//债权剩余天数(单位:天)
	
	private double zqSum;//债权金额(原本的价值)
	
	private double expectEarning;//预期收益
	
	private long buyTimestamp;//购买时间戳
	
	private long interestTimestamp;//计息时间戳
	
	private long endTimestamp;//到期时间戳
	
	private String productDetailUrl;//产品明细url
	
	private String[] contractUrl;//借款合同url
	
	private String contractInfoUrl;//合同页面链接
	
	private String assignmentAgreement;//债权转让及受让协议url

	private int kdbPlantId;//开店宝ID
	
	private String shopInfoUrl;//店铺信息页面URL

	public String getShopInfoUrl() {
		return shopInfoUrl;
	}

	public void setShopInfoUrl(String shopInfoUrl) {
		this.shopInfoUrl = shopInfoUrl;
	}
	
	public String getContractInfoUrl() {
		return contractInfoUrl;
	}

	public void setContractInfoUrl(String contractInfoUrl) {
		this.contractInfoUrl = contractInfoUrl;
	}

	public int getKdbPlantId() {
		return kdbPlantId;
	}

	public void setKdbPlantId(int kdbPlantId) {
		this.kdbPlantId = kdbPlantId;
	}

	public double getZqSum() {
		return zqSum;
	}

	public void setZqSum(double zqSum) {
		this.zqSum = zqSum;
	}

	public double getExpectEarning() {
		return expectEarning;
	}

	public void setExpectEarning(double expectEarning) {
		this.expectEarning = expectEarning;
	}

	public long getBuyTimestamp() {
		return buyTimestamp;
	}

	public void setBuyTimestamp(long buyTimestamp) {
		this.buyTimestamp = buyTimestamp;
	}

	public long getInterestTimestamp() {
		return interestTimestamp;
	}

	public void setInterestTimestamp(long interestTimestamp) {
		this.interestTimestamp = interestTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getProductDetailUrl() {
		return productDetailUrl;
	}

	public void setProductDetailUrl(String productDetailUrl) {
		this.productDetailUrl = productDetailUrl;
	}

	public String[] getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String[] contractUrl) {
		this.contractUrl = contractUrl;
	}

	public String getAssignmentAgreement() {
		return assignmentAgreement;
	}

	public void setAssignmentAgreement(String assignmentAgreement) {
		this.assignmentAgreement = assignmentAgreement;
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

	public double getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(double transferValue) {
		this.transferValue = transferValue;
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
	
}
