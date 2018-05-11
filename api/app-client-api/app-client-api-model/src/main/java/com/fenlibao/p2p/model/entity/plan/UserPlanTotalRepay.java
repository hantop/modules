package com.fenlibao.p2p.model.entity.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * t_user_plan_repay
 * @author Administrator
 *
 */
public class UserPlanTotalRepay {
    private int id;
    private int userId;
    private int planId;
    private int userPlanId;
    private int transactionType;
    private int term;
    private String state;
    private BigDecimal expectAmount;
    private BigDecimal actualAmount;
    private Date expectRepayDate;
    private Date actualRepayTime;
    
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
	public int getPlanId() {
		return planId;
	}
	public void setPlanId(int planId) {
		this.planId = planId;
	}
	public int getUserPlanId() {
		return userPlanId;
	}
	public void setUserPlanId(int userPlanId) {
		this.userPlanId = userPlanId;
	}
	public int getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public BigDecimal getExpectAmount() {
		return expectAmount;
	}
	public void setExpectAmount(BigDecimal expectAmount) {
		this.expectAmount = expectAmount;
	}
	public BigDecimal getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}
	public Date getExpectRepayDate() {
		return expectRepayDate;
	}
	public void setExpectRepayDate(Date expectRepayDate) {
		this.expectRepayDate = expectRepayDate;
	}
	public Date getActualRepayTime() {
		return actualRepayTime;
	}
	public void setActualRepayTime(Date actualRepayTime) {
		this.actualRepayTime = actualRepayTime;
	}
    
}
