package com.fenlibao.p2p.model.form.trade;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录
 * @author yangzengcai
 * @date 2015年8月15日
 */
public class TradeRecordForm {
    
	/**
	 * 交易时间
	 */
	private Date tradeTime;
	/**
	 * 交易金额
	 */
	private BigDecimal tradeAmount;
	/**
	 * 交易类型
	 */
	private String tradeTypeName;
	/**
	 * 备注
	 */
	private String mark;
	/**
	 * 
	 */
	private String balance; //结余
	
	
//	private String status = "交易成功"; //按产品需求，直接给默认
	
	
	public String getMark() {
		return mark;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Date getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getTradeTypeName() {
		return tradeTypeName;
	}
	public void setTradeTypeName(String tradeTypeName) {
		this.tradeTypeName = tradeTypeName;
	}
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}
	
	
}
