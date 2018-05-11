package com.fenlibao.p2p.model.vo.user;

import com.fenlibao.p2p.model.entity.user.RiskTestOption;
import com.fenlibao.p2p.model.entity.user.RiskTestQuestion;

import java.util.List;


/** 
 * @author: junda.feng
 * @date: 2016-6-23
 */
public class RiskTestQuestionVO {
	
	public String question;//问题
	
	public Integer sort;//排序
	
	public String subhead;//副标题
	
	public List<RiskTestOption> options;//选项

	public RiskTestQuestionVO(RiskTestQuestion question) {
		this.question=question.getQuestion();
		this.sort=question.getSort();
		this.subhead=question.getSubhead();
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

	public List<RiskTestOption> getOptions() {
		return options;
	}

	public void setOptions(List<RiskTestOption> options) {
		this.options = options;
	}
	
	
}

