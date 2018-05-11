package com.fenlibao.p2p.model.vo;

/**
 * 往期薪金宝计划列表
 */
public class SalaryPlanHistoryListVO{

	private static final long serialVersionUID = 1L;
	
	private int xjbId; //薪金宝id
	
	private int xjbPlanStatus; //薪金宝计划状态(0:收益中  1:可加入)
	
	private int investDay; //该薪金宝计划投资日(单位:日)
	
	private String xjbTitle; //薪金宝的title
	
	private double xjbYield;//薪金宝年化收益率
	
	private int xjbTime;//薪金宝产品期限
	
	private long timestamp;//薪金宝计划封标的时间戳

	public int getXjbId() {
		return xjbId;
	}

	public void setXjbId(int xjbId) {
		this.xjbId = xjbId;
	}

	public int getXjbPlanStatus() {
		return xjbPlanStatus;
	}

	public void setXjbPlanStatus(int xjbPlanStatus) {
		this.xjbPlanStatus = xjbPlanStatus;
	}

	public int getInvestDay() {
		return investDay;
	}

	public void setInvestDay(int investDay) {
		this.investDay = investDay;
	}

	public String getXjbTitle() {
		return xjbTitle;
	}

	public void setXjbTitle(String xjbTitle) {
		this.xjbTitle = xjbTitle;
	}


	public int getXjbTime() {
		return xjbTime;
	}

	public void setXjbTime(int xjbTime) {
		this.xjbTime = xjbTime;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getXjbYield() {
		return xjbYield;
	}

	public void setXjbYield(double xjbYield) {
		this.xjbYield = xjbYield;
	}

}
