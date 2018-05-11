package com.fenlibao.p2p.model.vo.bidinfo;

/** 
 * @Description: 证明文件
 * @author: junda.feng
 */
public class BidPublicAccessoryFilesVO{

	private int riskId;//风控类型id
	
	private String title; //文件title
	
	private String url; //法律文件HTML页面URL
	
	public int getRiskId() {
		return riskId;
	}

	public void setRiskId(int riskId) {
		this.riskId = riskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	



	
}
