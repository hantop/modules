package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RewardRecord {
	private Integer id;
	private Timestamp grantTime;
	private String grantName;
	private String operator;
	/**
	 * 0：未发放，1：已发放
	 */
	private byte granted;
	/**
	 * 1：体验金，2：现金红包 3：返现券 4：加息券
	 */
	private byte rewardType;
	/**
	 * 发送总数(成功)
	 */
	private Integer grantCount = 0;

	/**
	 * 发送总额(成功)
	 */
	private BigDecimal grantSum = BigDecimal.ZERO;

	/**
	 * 运行状态
	 */
	private byte inService;

	/**
	 * excel表原数
	 */
	private Integer grantCountTotal = 0;

	/**
	 * excel表原金额
	 */
	private BigDecimal grantSumTotal = BigDecimal.ZERO;

	/**
	 * 系统类型
	 */
	private int sysType;

	private String sysTypeLabel;

	private String countPersent;

	private String sumPersent;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getGrantTime() {
		return grantTime;
	}

	public void setGrantTime(Timestamp grantTime) {
		this.grantTime = grantTime;
	}

	public String getGrantName() {
		return grantName;
	}

	public void setGrantName(String grantName) {
		this.grantName = grantName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public byte getGranted() {
		return granted;
	}

	public void setGranted(byte granted) {
		this.granted = granted;
	}

	public byte getRewardType() {
		return rewardType;
	}

	public void setRewardType(byte rewardType) {
		this.rewardType = rewardType;
	}

	public byte getInService() {
		return inService;
	}

	public void setInService(byte inService) {
		this.inService = inService;
	}

	public int getSysType() {
		return sysType;
	}

	public void setSysType(int sysType) {
		this.sysType = sysType;
	}

	public String getSysTypeLabel() {
		return sysTypeLabel;
	}

	public void setSysTypeLabel(String sysTypeLabel) {
		this.sysTypeLabel = sysTypeLabel;
	}

	public Integer getGrantCountTotal() {
		return grantCountTotal;
	}

	public void setGrantCountTotal(Integer grantCountTotal) {
		this.grantCountTotal = grantCountTotal;
	}

	public BigDecimal getGrantSumTotal() {
		return grantSumTotal;
	}

	public void setGrantSumTotal(BigDecimal grantSumTotal) {
		this.grantSumTotal = grantSumTotal;
	}

	public String getCountPersent() {
		return countPersent;
	}

	public void setCountPersent(String countPersent) {
		this.countPersent = countPersent;
	}

	public String getSumPersent() {
		return sumPersent;
	}

	public void setSumPersent(String sumPersent) {
		this.sumPersent = sumPersent;
	}
}
