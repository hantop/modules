package com.fenlibao.p2p.model.mp.entity;

/**
 * @title  用户积分
 * @author laubrence
 * @date   2016-2-6 00:15:45
 */
public class MyPoint {
	
	int pointNum; //用户积分数量
	
	int pointStatus; //用户积分状态

	public int getPointNum() {
		return pointNum;
	}

	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}

	public int getPointStatus() {
		return pointStatus;
	}

	public void setPointStatus(int pointStatus) {
		this.pointStatus = pointStatus;
	}

}
