package com.fenlibao.p2p.common.util.pagination;

import java.io.Serializable;

public class Search implements Serializable {

	private static final long serialVersionUID = -9063839812170189904L;

	private int page = 1;

	private int limit = 15;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
