package com.fenlibao.p2p.model.entity.bid;

import java.util.List;

/** 
 * @Description: 标的证明文件
 * @author: junda.feng
 */
public class BidFiles{
	
	private int typeId;//风控类型id
	
	private String typename; //类型名称
	
	private List<String> FileCodes;//FileCode

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public List<String> getFileCodes() {
		return FileCodes;
	}

	public void setFileCodes(List<String> fileCodes) {
		FileCodes = fileCodes;
	}

	
}
