package com.fenlibao.p2p.model.entity.user;

/** P2P网络借贷风险承受力评估测试问题选项
 * @author: junda.feng
 * @date: 2016-6-23
 */
public class RiskTestOption {
	
//	public Integer qid;//问题id(risk_assessment_test_question.id)
	
	private String content;//问题
	
	private String score;//分值
	
	private Integer sort;//排序


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Integer getsort() {
		return sort;
	}

	public void setsort(Integer sort) {
		this.sort = sort;
	}


	
}

