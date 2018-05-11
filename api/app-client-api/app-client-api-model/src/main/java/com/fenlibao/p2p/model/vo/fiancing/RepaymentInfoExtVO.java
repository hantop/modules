package com.fenlibao.p2p.model.vo.fiancing;

import java.math.BigDecimal;
import java.util.Date;

import com.fenlibao.p2p.model.entity.finacing.RepaymentInfoExt;
import com.fenlibao.p2p.model.global.InterfaceConst;

/**
 * Created by junda.feng on 2016/6/16.
 */
public class RepaymentInfoExtVO {

    

	private BigDecimal principal;//本金
	
	private BigDecimal gains;//收益
	
	private Long date;//回款日期

	private String bidTitle;//标标题
	
	private int number;//期号
	
	private int periods;//期数

	private String repayment;//还款方式

	private int itemType;//区分类型(0:标  1:计划)

	private int planRecordId;//回款计划记录ID
	
	public RepaymentInfoExtVO(RepaymentInfoExt info) {
		this.principal=info.getPrincipal()==null?BigDecimal.ZERO:info.getPrincipal();
		this.gains=info.getGains();
		this.date="WH".equals(info.getState())||"HKZ".equals(info.getState()) ?
						(info.getExpectedRepaymentDate() == null ? 0 : info.getExpectedRepaymentDate().getTime()/1000) :
						(info.getActualRepaymentDate() == null ? 0 : info.getActualRepaymentDate().getTime()/1000);
		this.bidTitle=info.getCreditOrderId()==0?info.getBidTitle():InterfaceConst.CREDIT_NAME_PREFIX +info.getCreditCode();
		this.number=info.getNumber();
		this.periods=info.getPeriods();
		this.repayment=info.getRepayment();
		this.itemType=info.getItemType();
		this.planRecordId=info.getPlanRecordId();
	}

	public String getRepayment() {
		return repayment;
	}

	public void setRepayment(String repayment) {
		this.repayment = repayment;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public int getPlanRecordId() {
		return planRecordId;
	}

	public void setPlanRecordId(int planRecordId) {
		this.planRecordId = planRecordId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

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

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getBidTitle() {
		return bidTitle;
	}

	public void setBidTitle(String bidTitle) {
		this.bidTitle = bidTitle;
	}
}
