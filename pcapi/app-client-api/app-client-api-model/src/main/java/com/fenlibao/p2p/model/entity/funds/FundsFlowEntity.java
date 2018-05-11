package com.fenlibao.p2p.model.entity.funds;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金流水(t6102)
 * @author yangzengcai
 * @date 2015年11月19日
 */
public class FundsFlowEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4197829230389210623L;

	/**
	 * F02
	 * <p>资金账号ID，参考T6101.F01
	 */
	private Integer fundsAccountId;
	/**
	 * F03
	 * <p>交易类型ID，参考
	 */
	private Integer tradeTypeId;
	/**
	 * F04
	 * <p>对方账号ID，参考T6101.F01
	 */
	private Integer userId;
	/**
	 * F05
	 * <p>创建时间
	 */
	private Date createTime = new Date();
	/**
	 * F06
	 * <p>收入
	 */
	private BigDecimal income;
	/**
	 * F07
	 * <p>支出
	 */
	private BigDecimal expenditure;
	/**
	 * F08
	 * <p>余额
	 */
	private BigDecimal balance;
	/**
	 * F09
	 * <p>备注
	 */
	private String remark;
	/**
	 * F10
	 * <p>对账状态，WDZ:未对账；YDZ:已对账
	 */
	private String reconciliationStatus;
	/**
	 * F11
	 * <p>对账时间
	 */
	private Date reconciliationTime;
	/**
	 * F12
	 * <p>标ID
	 */
	private Integer bidId;
	
	
	public Integer getFundsAccountId() {
		return fundsAccountId;
	}
	public void setFundsAccountId(Integer fundsAccountId) {
		this.fundsAccountId = fundsAccountId;
	}
	public Integer getTradeTypeId() {
		return tradeTypeId;
	}
	public void setTradeTypeId(Integer tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getIncome() {
		return income;
	}
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	public BigDecimal getExpenditure() {
		return expenditure;
	}
	public void setExpenditure(BigDecimal expenditure) {
		this.expenditure = expenditure;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReconciliationStatus() {
		return reconciliationStatus;
	}
	public void setReconciliationStatus(String reconciliationStatus) {
		this.reconciliationStatus = reconciliationStatus;
	}
	public Date getReconciliationTime() {
		return reconciliationTime;
	}
	public void setReconciliationTime(Date reconciliationTime) {
		this.reconciliationTime = reconciliationTime;
	}
	public Integer getBidId() {
		return bidId;
	}
	public void setBidId(Integer bidId) {
		this.bidId = bidId;
	}
	
}
