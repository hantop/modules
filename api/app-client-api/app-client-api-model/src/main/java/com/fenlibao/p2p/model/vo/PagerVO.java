package com.fenlibao.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页信息
 * @param <T>
 */
public class PagerVO<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int curpage;
	
	private int totalpage;

	private List<T> items;
	
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	
	
}
