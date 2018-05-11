package com.fenlibao.p2p.model.entity.user;

/** P2P网络借贷风险承受力评估测试问题
 * @author: junda.feng
 * @date: 2016-6-23
 */
public class RiskTestQuestion {
	
	private Integer id;//
	
	private String question;//问题
	
	private Integer sort;//排序
	
	private String subhead;//副标题

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}
	
	
	

}

