package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 新手标信息
 */
public class NoviceBidVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private int bidId;//标的id
	
	private String noviceBidTitle;//新手标标题
	
	private String yield;//新手标收益率
	
	private String timestamp;//筹款到期时间
	
    private int isNoviceBid;//是否是新手标 0:非新手标  1：新手标
	
	private int loanDays;//借款周期（单位：天）

	private String[] assetTypes;//资产类型（信用认证、实地认证、抵押担保）
	
	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public String getNoviceBidTitle() {
		return noviceBidTitle;
	}

	public void setNoviceBidTitle(String noviceBidTitle) {
		this.noviceBidTitle = noviceBidTitle;
	}

	public String getYield() {
		return yield;
	}

	public void setYield(String yield) {
		this.yield = yield;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getIsNoviceBid() {
		return isNoviceBid;
	}

	public void setIsNoviceBid(int isNoviceBid) {
		this.isNoviceBid = isNoviceBid;
	}

	public int getLoanDays() {
		return loanDays;
	}

	public void setLoanDays(int loanDays) {
		this.loanDays = loanDays;
	}

	public String[] getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(String[] assetTypes) {
		this.assetTypes = assetTypes;
	}
	
}
