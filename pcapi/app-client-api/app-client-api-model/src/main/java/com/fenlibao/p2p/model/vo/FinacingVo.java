package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 债权
 */
public class FinacingVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int zqId;//债权ID
	
	private String zqTitle;//债权名称
	
	private double zqSum;//投资金额
	
	private String zqYield;//收益率
	
	private int zqTime;//债权期限
	
	private double zqEarning;//已获收益
	
	private int zqStatus;//债权状态(0:收益中  1:转让中)
	
	private long buyTimestamp;//购买时间
		
	private int isNoviceBid;//是否是新手标 (1:是新手标;0:普通开店宝标)
	
	private int loanDays;//借款周期（单位：天）

	public int getZqId() {
		return zqId;
	}

	public void setZqId(int zqId) {
		this.zqId = zqId;
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

	public double getZqEarning() {
		return zqEarning;
	}

	public void setZqEarning(double zqEarning) {
		this.zqEarning = zqEarning;
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
