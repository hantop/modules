package com.fenlibao.p2p.model.entity.bid;

/**
 * 标扩展组信息
 * @author laubrence
 * @date   2016-3-2 10:12:40
 *
 */
public class BidExtendGroupInfo {
	
	public int groupId ;  //组id
	
	public String groupName; //组名称
	
	public String groupCode;  //组编码

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

}
