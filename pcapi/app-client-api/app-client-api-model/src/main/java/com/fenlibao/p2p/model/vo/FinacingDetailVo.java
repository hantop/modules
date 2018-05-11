package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 债权
 */
public class FinacingDetailVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int kdbId;//债权ID
	
	private String zqTitle;//债权名称
	
	private double zqSum;//投资金额
	
	private double originalMoney;//原始债权金额
	
	private String zqYield;//收益率
	
	private int zqTime;//债权期限
	
	private double zqEarning;//所有收益（预计）
	
	private int zqStatus;//债权状态(0:收益中  1:转让中)
	
	private long buyTimestamp;//购买时间  申购时间戳
		
	private long interestTimestamp;//计息时间戳
	
	private long endTimestamp;//到期时间戳
	
	private String shopDetailUrl; //产品明细url
	
	private String serviceAgreement; //开店宝服务协议url
	
	private String[] contractUrl;// 合同图片信息
	
	private String contractInfoUrl;//合同页面链接
	
	private double passedEarning;//过去天数的收益
	
	private int surplusDays;//债权剩余天数
	
	private int isNoviceBid;//是否是新手标 (1:是新手标;0:普通开店宝标)
	
	private int loanDays;//借款周期（单位：天）
	
	public double getOriginalMoney() {
		return originalMoney;
	}

	public void setOriginalMoney(double originalMoney) {
		this.originalMoney = originalMoney;
	}

	public int getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(int surplusDays) {
		this.surplusDays = surplusDays;
	}

	public String getContractInfoUrl() {
		return contractInfoUrl;
	}

	public void setContractInfoUrl(String contractInfoUrl) {
		this.contractInfoUrl = contractInfoUrl;
	}

	private String shopInfoUrl;//店铺信息URL

	public String getShopInfoUrl() {
		return shopInfoUrl;
	}

	public void setShopInfoUrl(String shopInfoUrl) {
		this.shopInfoUrl = shopInfoUrl;
	}

	public String[] getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String[] contractUrl) {
		this.contractUrl = contractUrl;
	}

	public String getZqTitle() {
		return zqTitle;
	}

	public void setZqTitle(String zqTitle) {
		this.zqTitle = zqTitle;
	}

	public double getZqSum() {
		return zqSum;
	}

	public void setZqSum(double zqSum) {
		this.zqSum = zqSum;
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

	public int getZqStatus() {
		return zqStatus;
	}

	public void setZqStatus(int zqStatus) {
		this.zqStatus = zqStatus;
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

	public String getShopDetailUrl() {
		return shopDetailUrl;
	}

	public void setShopDetailUrl(String shopDetailUrl) {
		this.shopDetailUrl = shopDetailUrl;
	}

	public double getZqEarning() {
		return zqEarning;
	}

	public void setZqEarning(double zqEarning) {
		this.zqEarning = zqEarning;
	}

	public String getServiceAgreement() {
		return serviceAgreement;
	}

	public void setServiceAgreement(String serviceAgreement) {
		this.serviceAgreement = serviceAgreement;
	}

	public int getKdbId() {
		return kdbId;
	}

	public void setKdbId(int kdbId) {
		this.kdbId = kdbId;
	}

	public double getPassedEarning() {
		return passedEarning;
	}

	public void setPassedEarning(double passedEarning) {
		this.passedEarning = passedEarning;
	}

	/**
	 * @return the isNoviceBid
	 */
	public int getIsNoviceBid() {
		return isNoviceBid;
	}

	/**
	 * @param isNoviceBid the isNoviceBid to set
	 */
	public void setIsNoviceBid(int isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	/**
	 * @return the loanDays
	 */
	public int getLoanDays() {
		return loanDays;
	}

	/**
	 * @param loanDays the loanDays to set
	 */
	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}
	
}
