package com.fenlibao.p2p.model.vo.bidinfo;

import java.util.List;


public class BidExtendGroupVO {
	
	public String groupName;  
	
	public String groupCode;
	
	public List<BidExtendGroupItemVO> itemInfoList;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<BidExtendGroupItemVO> getItemInfoList() {
		return itemInfoList;
	}

	public void setItemInfoList(List<BidExtendGroupItemVO> itemInfoList) {
		this.itemInfoList = itemInfoList;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

}
