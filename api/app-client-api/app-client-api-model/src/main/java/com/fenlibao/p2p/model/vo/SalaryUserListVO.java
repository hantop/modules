package com.fenlibao.p2p.model.vo;

import java.math.BigDecimal;

/**
 * 往期薪金宝计划列表
 */
public class SalaryUserListVO{

	private static final long serialVersionUID = 1L;
	
	private int userXjbId; //薪金宝id
	
	private String xjbTitle; //薪金宝的title
	
	private double xjbYield;//薪金宝年化收益率
	
	private int xjbTime;//薪金宝产品期限
	
	private long timestamp;//薪金宝计划开启的时间戳
	
	private long buyTimestamp;//用户薪金宝加入购买时间戳
	
	private long endTimestamp;//用户薪金宝的到期时间戳
	
	private long investTimestamp;//用户下个月投资时间
	
	private BigDecimal investSum;//该用户每个月投资金额
	
	private BigDecimal totalSum;//该薪金宝计划累计投资总额
	
	private BigDecimal xjbEaring;//该薪金宝计划已获收益
	
	private int investMonths;//薪金宝购买月数
	
	private int validStatus;//薪金宝本月购买是否过期
	
	private int canbuy; //本月是否可以购买
	
	private String serviceAgreement;

	public int getUserXjbId() {
		return userXjbId;
	}

	public void setUserXjbId(int userXjbId) {
		this.userXjbId = userXjbId;
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

	public long getBuyTimestamp() {
		return buyTimestamp;
	}

	public void setBuyTimestamp(long buyTimestamp) {
		this.buyTimestamp = buyTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public long getInvestTimestamp() {
		return investTimestamp;
	}

	public void setInvestTimestamp(long investTimestamp) {
		this.investTimestamp = investTimestamp;
	}

	public int getInvestMonths() {
		return investMonths;
	}

	public void setInvestMonths(int investMonths) {
		this.investMonths = investMonths;
	}

	public int getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(int validStatus) {
		this.validStatus = validStatus;
	}

	public double getXjbYield() {
		return xjbYield;
	}

	public void setXjbYield(double xjbYield) {
		this.xjbYield = xjbYield;
	}

	public int getCanbuy() {
		return canbuy;
	}

	public void setCanbuy(int canbuy) {
		this.canbuy = canbuy;
	}

	/**
	 * @return the totalSum
	 */
	public BigDecimal getTotalSum() {
		return totalSum;
	}

	/**
	 * @param totalSum the totalSum to set
	 */
	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}

	/**
	 * @return the investSum
	 */
	public BigDecimal getInvestSum() {
		return investSum;
	}

	/**
	 * @param investSum the investSum to set
	 */
	public void setInvestSum(BigDecimal investSum) {
		this.investSum = investSum;
	}

	/**
	 * @return the xjbEaring
	 */
	public BigDecimal getXjbEaring() {
		return xjbEaring;
	}

	/**
	 * @param xjbEaring the xjbEaring to set
	 */
	public void setXjbEaring(BigDecimal xjbEaring) {
		this.xjbEaring = xjbEaring;
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