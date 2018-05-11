package com.fenlibao.p2p.model.mp.entity;

import java.util.Date;

public class ConsumeExchangePointsInfo {
	
	public int pointUnitNum; //一元人民币消费兑换积分数量
	
	public Date startTime; //开始时间
	
	public Date endTime; //结束时间

	public int getPointUnitNum() {
		return pointUnitNum;
	}

	public void setPointUnitNum(int pointUnitNum) {
		this.pointUnitNum = pointUnitNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
