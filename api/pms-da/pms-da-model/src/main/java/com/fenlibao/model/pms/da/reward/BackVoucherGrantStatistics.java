package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;

public class BackVoucherGrantStatistics {
	private String activityCode;
	private Integer grantCount;
	private BigDecimal grantSum;

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public Integer getGrantCount() {
		return grantCount;
	}

	public void setGrantCount(Integer grantCount) {
		this.grantCount = grantCount;
	}

	public BigDecimal getGrantSum() {
		return grantSum;
	}

	public void setGrantSum(BigDecimal grantSum) {
		this.grantSum = grantSum;
	}

}
