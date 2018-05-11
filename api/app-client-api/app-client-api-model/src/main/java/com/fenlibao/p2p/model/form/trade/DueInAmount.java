package com.fenlibao.p2p.model.form.trade;

import java.math.BigDecimal;

/**
 * 待收金额
 * @author yangzengcai
 * @date 2015年10月13日
 */
public class DueInAmount {

	/**
	 * 本金
	 */
	private BigDecimal principal = BigDecimal.ZERO;
	
	/**
	 * 收益
	 */
	private BigDecimal gains = BigDecimal.ZERO;

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getGains() {
		return gains;
	}

	public void setGains(BigDecimal gains) {
		this.gains = gains;
	}
}
