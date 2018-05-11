package com.fenlibao.p2p.model.mp.entity;

import java.math.BigDecimal;

public class UserFundsAccountInfo {
	
	int fundsAccountId; //资金账户id
	
	int userId; //用户id
	
	BigDecimal fundsAmount; //资金账户余额

	public int getFundsAccountId() {
		return fundsAccountId;
	}

	public void setFundsAccountId(int fundsAccountId) {
		this.fundsAccountId = fundsAccountId;
	}

	public BigDecimal getFundsAmount() {
		return fundsAmount;
	}

	public void setFundsAmount(BigDecimal fundsAmount) {
		this.fundsAmount = fundsAmount;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
