package com.fenlibao.p2p.model.mp.entity;

import java.util.Date;

public class MyPointExchangeDetail {
	
	public String pName; //积分类型名称

	public int pNum; //积分数量
	
	public Date exchangeTime; // 创建时间

	public int exchangeQty = 1; // 兑换数量

	public String exchangeState; // 兑换状态

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public int getpNum() {
		return pNum;
	}

	public void setpNum(int pNum) {
		this.pNum = pNum;
	}

	public Date getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(Date exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public int getExchangeQty() {
		return exchangeQty;
	}

	public void setExchangeQty(int exchangeQty) {
		this.exchangeQty = exchangeQty;
	}

	public String getExchangeState() {
		return exchangeState;
	}

	public void setExchangeState(String exchangeState) {
		this.exchangeState = exchangeState;
	}
}
