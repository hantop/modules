package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;
import java.util.Date;

public class UserExperience {
	private Integer id;
	private Integer userId;
	private Integer goldId;
	private Byte status;
	private String goldOrigin;
	private Date startTime;
	private BigDecimal yearYield;
	private Date endTime;
	private String yieldStatus;
	private Integer grantId;
	private String activityCode;
	private Byte grantStatus;
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Byte getGrantStatus() {
		return grantStatus;
	}

	public void setGrantStatus(Byte grantStatus) {
		this.grantStatus = grantStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGoldId() {
		return goldId;
	}

	public void setGoldId(Integer goldId) {
		this.goldId = goldId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getGoldOrigin() {
		return goldOrigin;
	}

	public void setGoldOrigin(String goldOrigin) {
		this.goldOrigin = goldOrigin;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getYearYield() {
		return yearYield;
	}

	public void setYearYield(BigDecimal yearYield) {
		this.yearYield = yearYield;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getYieldStatus() {
		return yieldStatus;
	}

	public void setYieldStatus(String yieldStatus) {
		this.yieldStatus = yieldStatus;
	}

	public Integer getGrantId() {
		return grantId;
	}

	public void setGrantId(Integer grantId) {
		this.grantId = grantId;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}



}
