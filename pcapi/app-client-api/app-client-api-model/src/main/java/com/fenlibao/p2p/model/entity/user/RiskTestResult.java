package com.fenlibao.p2p.model.entity.user;

/** P2P网络借贷风险承受力评估测试问题结果
 * @author: junda.feng
 * @date: 2016-6-23
 */
public class RiskTestResult {
	
	private int id;//id
	
	private String type;//风险类型
	
	private String result;//评估结果
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	
}

