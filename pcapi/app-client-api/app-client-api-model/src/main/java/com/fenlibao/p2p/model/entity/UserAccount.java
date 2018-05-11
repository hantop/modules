package com.fenlibao.p2p.model.entity;

import java.math.BigDecimal;

/***
 * 用户资金账户(t6101)
 */
public class UserAccount {

	private int accountId;//账户ID
	private int userId;//用户ID
	private BigDecimal balance;//账户余额
	private String type;//账户类型
	private String account;// 资金账号
	private String accountName;// 账户名称
	private String lastUpdatetime;// 最后更新时间

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getLastUpdatetime() {
		return lastUpdatetime;
	}

	public void setLastUpdatetime(String lastUpdatetime) {
		this.lastUpdatetime = lastUpdatetime;
	}
}
