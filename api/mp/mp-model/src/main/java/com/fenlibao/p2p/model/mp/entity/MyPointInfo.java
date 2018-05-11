package com.fenlibao.p2p.model.mp.entity;

import java.math.BigDecimal;

/**
 * 用户积分 by kris 20161113
 */
public class MyPointInfo {
	
	int myPoint; //可用积分额度
	int usedPoints; //已使用积分
	int availablePoint;//实际可用积分
	int totallPoint; //总积分
	String totallInvestAmout;//累计投资
	int pointStatus; //用户积分状态 (1：可用；0：禁用；2：冻结中)',

	public int getAvailablePoint() {
		return availablePoint;
	}

	public void setAvailablePoint(int availablePoint) {
		this.availablePoint = availablePoint;
	}

	public int getMyPoint() {
		return myPoint;
	}

	public String getTotallInvestAmout() {
		return totallInvestAmout;
	}

	public void setTotallInvestAmout(String totallInvestAmout) {
		this.totallInvestAmout = totallInvestAmout;
	}

	public void setMyPoint(int myPoint) {
		this.myPoint = myPoint;
	}

	public int getTotallPoint() {
		return totallPoint;
	}

	public void setTotallPoint(int totallPoint) {
		this.totallPoint = totallPoint;
	}


	public int getPointStatus() {
		return pointStatus;
	}

	public void setPointStatus(int pointStatus) {
		this.pointStatus = pointStatus;
	}

}
