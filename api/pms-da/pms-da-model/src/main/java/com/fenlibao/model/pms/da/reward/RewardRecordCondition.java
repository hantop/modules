package com.fenlibao.model.pms.da.reward;

public class RewardRecordCondition {
	private String startDate;
	private String endDate;
	private String grantName;
	/**
	 * 1：体验金，2：现金红包，3：返现券，4：加息券
	 */
	private Byte rewardType;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getGrantName() {
		return grantName;
	}

	public void setGrantName(String grantName) {
		this.grantName = grantName;
	}

	public Byte getRewardType() {
		return rewardType;
	}

	public void setRewardType(Byte rewardType) {
		this.rewardType = rewardType;
	}



}
