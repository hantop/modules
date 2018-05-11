package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by junda.feng on 2016/6/17.
 */
public class PlanRepaymentDetail {
    
	private BigDecimal amount;//回款金额
	
	private String detailName;//回款名称

	private Date time;//回款时间

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
