package com.fenlibao.p2p.model.entity;

import java.io.Serializable;

public class ShopInformation implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int bidId;//标ID

    private String shopName;//店铺名称
	
	private String shopAddress;//店铺地址
	
	private String shopPicture;//店铺展示图片

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getShopPicture() {
		return shopPicture;
	}

	public void setShopPicture(String shopPicture) {
		this.shopPicture = shopPicture;
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}
	
}
