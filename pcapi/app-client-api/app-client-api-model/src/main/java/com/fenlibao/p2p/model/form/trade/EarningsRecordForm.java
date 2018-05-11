package com.fenlibao.p2p.model.form.trade;

import java.math.BigDecimal;

/**
 * 收益记录
 * @author yangzengcai
 * @date 2015年8月15日
 */
public class EarningsRecordForm {
	
	public static final String HISTORY = "已到账利息";
	
	public static final String EXPECT = "未到账利息";
	
	public static final String TOTAL = "合计";

	/**
	 * 收益类别名称
	 */
	private String earningsTypeName;
	
	/**
	 * 收益金额
	 */
	private BigDecimal amount;
	
	private Integer code;

	public String getEarningsTypeName() {
		return earningsTypeName;
	}

	public void setEarningsTypeName(String earningsTypeName) {
		this.earningsTypeName = earningsTypeName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
