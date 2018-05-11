package com.fenlibao.model.pms.da.finance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代充值
 * 
 */
public class ReplacementRecharge {
	private int id;// 自增ID
	private int userId;
	private String userRole;// 用户角色
	private String userType;// 用户类型
	private String account;// 账户
	private String userName;// 名称
	private BigDecimal rechargeMoney;// 代充值金额
	private String rechargeUserName;// 充值人
	private String auditUserName;// 审核人
	private int status;// 状态 0, 审核不通过; 1,审核通过(充值成功[11]/充值失败[10]); 2,待审核;
	private int xwRequestId;
	private String orderState;// 订单状态
	private Date rechargeTime;// 充值时间
	private Date auditTime;// 审核时间
	private int rewardRecordId;// 现金奖励发放记录

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(BigDecimal rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public String getRechargeUserName() {
		return rechargeUserName;
	}

	public void setRechargeUserName(String rechargeUserName) {
		this.rechargeUserName = rechargeUserName;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getXwRequestId() {
		return xwRequestId;
	}

	public void setXwRequestId(int xwRequestId) {
		this.xwRequestId = xwRequestId;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public Date getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Date rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public int getRewardRecordId() {
		return rewardRecordId;
	}

	public void setRewardRecordId(int rewardRecordId) {
		this.rewardRecordId = rewardRecordId;
	}
}
