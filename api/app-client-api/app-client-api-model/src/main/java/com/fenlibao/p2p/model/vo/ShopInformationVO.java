package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 店铺信息
 */
public class ShopInformationVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String shopName;//店铺名称
	
	private String shopAddress;//店铺地址
	
	private String[] shopPictures;//店铺展示图片

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

	public String[] getShopPictures() {
		return shopPictures;
	}

	public void setShopPictures(String[] shopPictures) {
		this.shopPictures = shopPictures;
	}
}
