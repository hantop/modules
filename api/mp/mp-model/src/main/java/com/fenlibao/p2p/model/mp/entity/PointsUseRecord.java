package com.fenlibao.p2p.model.mp.entity;

import java.math.BigDecimal;

public class PointsUseRecord {
	
	public int typeId; //积分类型id
	
	public int numbers; //积分使用数量
	
	public BigDecimal amount; //积分兑换金额
	
	public int exchangeStatus; //积分兑换状态(1:已申请;2:兑换成功;3:兑换失败)

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getNumbers() {
		return numbers;
	}

	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getExchangeStatus() {
		return exchangeStatus;
	}

	public void setExchangeStatus(int exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}

}
