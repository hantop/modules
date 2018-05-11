package com.fenlibao.p2p.model.entity.bid;

/**
 * 标扩展组每项信息
 * @author laubrence
 * @date   2016-3-2 10:12:40
 *
 */
public class BidExtendGroupItemInfo {
	
	public String itemName; //每项名称
	
	public String itemValue; //每项值
	
	public String itemCode; //每项编码
	
	public String itemType; //每项类型

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
}
