package com.fenlibao.p2p.model.mp.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @title  积分兑换现金实体
 * @author laubrence
 * @date   2016-2-9 17:29:20
 */
public class PointsExchangeCashInfo {
	
	public int typeId; //积分类型id
	
	public int totalUseFrequency; //总使用次数
	
	public int maxUseNumber; //最大使用积分数量
	
	public int minUseNumber; //最低使用积分数量
	
	public BigDecimal exchangeAmount; //一积分兑换金额
	
	public int isByYear ; //是否按年
	
	public int yearFrequency ; //年使用频率
	
	public int isByMonth ; //是否按月
	
	public int monthFrequency ; //月使用频率
	
	public int isByDay ; //是否按日

	public int dayFrequency ; //日使用频率
	
	public Date startTime; //开始时间
	
	public Date endTime; //结束时间

	public int getTotalUseFrequency() {
		return totalUseFrequency;
	}

	public void setTotalUseFrequency(int totalUseFrequency) {
		this.totalUseFrequency = totalUseFrequency;
	}

	public int getMaxUseNumber() {
		return maxUseNumber;
	}

	public void setMaxUseNumber(int maxUseNumber) {
		this.maxUseNumber = maxUseNumber;
	}

	public BigDecimal getExchangeAmount() {
		return exchangeAmount;
	}

	public void setExchangeAmount(BigDecimal exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
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

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getIsByYear() {
		return isByYear;
	}

	public void setIsByYear(int isByYear) {
		this.isByYear = isByYear;
	}

	public int getYearFrequency() {
		return yearFrequency;
	}

	public void setYearFrequency(int yearFrequency) {
		this.yearFrequency = yearFrequency;
	}

	public int getIsByMonth() {
		return isByMonth;
	}

	public void setIsByMonth(int isByMonth) {
		this.isByMonth = isByMonth;
	}

	public int getMonthFrequency() {
		return monthFrequency;
	}

	public void setMonthFrequency(int monthFrequency) {
		this.monthFrequency = monthFrequency;
	}

	public int getIsByDay() {
		return isByDay;
	}

	public void setIsByDay(int isByDay) {
		this.isByDay = isByDay;
	}

	public int getDayFrequency() {
		return dayFrequency;
	}

	public void setDayFrequency(int dayFrequency) {
		this.dayFrequency = dayFrequency;
	}

	/**
	 * @return the minUseNumber
	 */
	public int getMinUseNumber() {
		return minUseNumber;
	}

	/**
	 * @param minUseNumber the minUseNumber to set
	 */
	public void setMinUseNumber(int minUseNumber) {
		this.minUseNumber = minUseNumber;
	}
}
