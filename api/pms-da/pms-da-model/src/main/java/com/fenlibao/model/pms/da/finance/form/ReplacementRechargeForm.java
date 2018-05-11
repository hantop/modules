package com.fenlibao.model.pms.da.finance.form;

/**
 * 代充值记录表单
 * 
 */
public class ReplacementRechargeForm {

	private int userId;// 用户id
	private String account;// 账号
	private String userName;// 用户名
	private String userType;// 用户类型
	private String userRole;// 账户类型
	private String status;// 状态

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
