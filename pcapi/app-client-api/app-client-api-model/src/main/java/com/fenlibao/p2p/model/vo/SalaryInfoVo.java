package com.fenlibao.p2p.model.vo;

import java.math.BigDecimal;

/**
 * 薪金宝详情结果
 */
public class SalaryInfoVo extends SalaryVo{

	private static final long serialVersionUID = 1L;
	
	private long buyTimestamp;//加入时间戳
	
	private long interestTimestamp;//计息时间戳
	
	private long endTimestamp;//到期时间戳
	
	private String cpmxUrl;//产品明细url
	
	private BigDecimal totalSum;//总筹款金额
	
	private BigDecimal investSum;//已投金额

	public long getBuyTimestamp() {
		return buyTimestamp;
	}

	public void setBuyTimestamp(long buyTimestamp) {
		this.buyTimestamp = buyTimestamp;
	}

	public long getInterestTimestamp() {
		return interestTimestamp;
	}

	public void setInterestTimestamp(long interestTimestamp) {
		this.interestTimestamp = interestTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getCpmxUrl() {
		return cpmxUrl;
	}

	public void setCpmxUrl(String cpmxUrl) {
		this.cpmxUrl = cpmxUrl;
	}

	public BigDecimal getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(BigDecimal totalSum) {
		this.totalSum = totalSum;
	}

	public BigDecimal getInvestSum() {
		return investSum;
	}

	public void setInvestSum(BigDecimal investSum) {
		this.investSum = investSum;
	}

}
