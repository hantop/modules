package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 薪金宝信息结果
 */
public class SalaryVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int xjbId;//薪金宝计划id
	
	private int xjbqtSum;//薪金宝起投金额
	
	private String xjbTitle;//薪金宝的title
	
	private String xjbYield;//薪金宝年化收益率
	
	private int xjbTime;//薪金宝产品期限
	
	private int investDay;//薪金宝计划投资日
	
	private int canbuy; //当前薪金宝是否可加入还是续买
	
	private String serviceAgreement;//服务协议

	public int getXjbId() {
		return xjbId;
	}

	public void setXjbId(int xjbId) {
		this.xjbId = xjbId;
	}

	public int getXjbqtSum() {
		return xjbqtSum;
	}

	public void setXjbqtSum(int xjbqtSum) {
		this.xjbqtSum = xjbqtSum;
	}

	public String getXjbTitle() {
		return xjbTitle;
	}

	public void setXjbTitle(String xjbTitle) {
		this.xjbTitle = xjbTitle;
	}

	public String getXjbYield() {
		return xjbYield;
	}

	public void setXjbYield(String xjbYield) {
		this.xjbYield = xjbYield;
	}

	public int getXjbTime() {
		return xjbTime;
	}

	public void setXjbTime(int xjbTime) {
		this.xjbTime = xjbTime;
	}

	public int getInvestDay() {
		return investDay;
	}

	public void setInvestDay(int investDay) {
		this.investDay = investDay;
	}

	public int getCanbuy() {
		return canbuy;
	}

	public void setCanbuy(int canbuy) {
		this.canbuy = canbuy;
	}

	/**
	 * @return the serviceAgreement
	 */
	public String getServiceAgreement() {
		return serviceAgreement;
	}

	/**
	 * @param serviceAgreement the serviceAgreement to set
	 */
	public void setServiceAgreement(String serviceAgreement) {
		this.serviceAgreement = serviceAgreement;
	}


	
}
