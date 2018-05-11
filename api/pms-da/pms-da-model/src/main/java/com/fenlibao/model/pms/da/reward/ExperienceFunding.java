package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ExperienceFunding {
    private int id;
    private int userId;
    private Timestamp createTime;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal overage;
    private String remark;
    private String operatorType;
    private int expId;
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
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getIncome() {
		return income;
	}
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	public BigDecimal getExpenses() {
		return expenses;
	}
	public void setExpenses(BigDecimal expenses) {
		this.expenses = expenses;
	}
	public BigDecimal getOverage() {
		return overage;
	}
	public void setOverage(BigDecimal overage) {
		this.overage = overage;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	public int getExpId() {
		return expId;
	}
	public void setExpId(int expId) {
		this.expId = expId;
	}
    
}
