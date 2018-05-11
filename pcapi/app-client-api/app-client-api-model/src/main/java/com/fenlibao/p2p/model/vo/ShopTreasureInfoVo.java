package com.fenlibao.p2p.model.vo;

import java.math.BigDecimal;

/**
 * 开店宝详情
 */
public class ShopTreasureInfoVo extends ShopTreasureVo {

	private BigDecimal balance;//用户  余额
	
	private String kdbfwxyUrl;//开店宝服务协议url
	
	private String cpmxUrl;//产品明细url
	
    private long buyTimestamp;//申购时间戳
	
	private long interestTimestamp;//计息时间戳
	
	private long endTimestamp;//到期时间戳
	
	private int canbuy; //判断当前用户是否可以申购
	
	private String shopInfoUrl;//店铺信息页面URL
	
	private String remark;//项目描述
	
	private String[] contractUrl;//合约文件URL
	
	public String getShopInfoUrl() {
		return shopInfoUrl;
	}

	public void setShopInfoUrl(String shopInfoUrl) {
		this.shopInfoUrl = shopInfoUrl;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
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

	public String getKdbfwxyUrl() {
		return kdbfwxyUrl;
	}

	public void setKdbfwxyUrl(String kdbfwxyUrl) {
		this.kdbfwxyUrl = kdbfwxyUrl;
	}

	public String getCpmxUrl() {
		return cpmxUrl;
	}

	public void setCpmxUrl(String cpmxUrl) {
		this.cpmxUrl = cpmxUrl;
	}

	/**
	 * @return the canbuy
	 */
	public int getCanbuy() {
		return canbuy;
	}

	/**
	 * @param canbuy the canbuy to set
	 */
	public void setCanbuy(int canbuy) {
		this.canbuy = canbuy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String[] getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String[] contractUrl) {
		this.contractUrl = contractUrl;
	}
	
}
