package com.fenlibao.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

public class TenderRecordResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private double userTotalInvest;//当前用户的总的投标金额
	
	private List<TenderRecordVO> items;

	public double getUserTotalInvest() {
		return userTotalInvest;
	}

	public void setUserTotalInvest(double userTotalInvest) {
		this.userTotalInvest = userTotalInvest;
	}

	public List<TenderRecordVO> getItems() {
		return items;
	}

	public void setItems(List<TenderRecordVO> items) {
		this.items = items;
	}
	
}
