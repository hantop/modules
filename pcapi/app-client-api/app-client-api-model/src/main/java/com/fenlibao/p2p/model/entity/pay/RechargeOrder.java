package com.fenlibao.p2p.model.entity.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S65.enums.T6501_F03;

public class RechargeOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2989258678905744477L;

	/**
	 * 订单id
	 */
	private int id;
	/**
	 * 下单时间(T6501)
	 */
	private Timestamp createTime;
	/**
	 * 充值金额
	 */
	private BigDecimal amount;
	/**
	 * 支付公司代码
	 */
	private int payCompanyCode;
	/**
	 * 支付状态
	 */
	private T6501_F03 status;
	/**
	 * added by zcai 20150824
	 * 用户ID,参考T6110.F01
	 */
	private int userId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getPayCompanyCode() {
		return payCompanyCode;
	}
	public void setPayCompanyCode(int payCompanyCode) {
		this.payCompanyCode = payCompanyCode;
	}
	public T6501_F03 getStatus() {
		return status;
	}
	public void setStatus(T6501_F03 status) {
		this.status = status;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
