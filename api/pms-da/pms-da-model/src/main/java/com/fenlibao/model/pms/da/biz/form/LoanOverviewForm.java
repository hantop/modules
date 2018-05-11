package com.fenlibao.model.pms.da.biz.form;

import java.util.List;

public class LoanOverviewForm {
	/**
	 * 发标开始日期
	 */
	private String tenderStartDate;
	/**
	 * 发标结束日期
	 */
	private String tenderEndDate;
	/**
	 * 放款开始日期
	 */
	private String loanStartDate;
	/**
	 * 放款结束日期
	 */
	private String loanEndDate;
	/**
	 * 还款开始日期
	 */
	private String repayStartDate;
	/**
	 * 还款结束日期
	 */
	private String repayEndDate;
	/**
	 * 借款状态
	 */
	private String bidStatus;
	/**
	 * 借款标题
	 */
	private String bidTitle;
	/**
	 * 借款账户
	 */
	private String borrowerAccount;
	/**
	 * 借款人姓名
	 */
	private String borrowerName;
	/**
	 * 排序字段
	 */
	private String sort;
	/**
	 * 产品类型
	 */
	private String productType;
	private List<Integer> productTypes;

	private String sysType;// 系统类型

	public String getTenderStartDate() {
		return tenderStartDate;
	}

	public void setTenderStartDate(String tenderStartDate) {
		this.tenderStartDate = tenderStartDate;
	}

	public String getTenderEndDate() {
		return tenderEndDate;
	}

	public void setTenderEndDate(String tenderEndDate) {
		this.tenderEndDate = tenderEndDate;
	}

	public String getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(String loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public String getLoanEndDate() {
		return loanEndDate;
	}

	public void setLoanEndDate(String loanEndDate) {
		this.loanEndDate = loanEndDate;
	}

	public String getRepayStartDate() {
		return repayStartDate;
	}

	public void setRepayStartDate(String repayStartDate) {
		this.repayStartDate = repayStartDate;
	}

	public String getRepayEndDate() {
		return repayEndDate;
	}

	public void setRepayEndDate(String repayEndDate) {
		this.repayEndDate = repayEndDate;
	}

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}

	public String getBidTitle() {
		return bidTitle;
	}

	public void setBidTitle(String bidTitle) {
		this.bidTitle = bidTitle;
	}

	public String getBorrowerAccount() {
		return borrowerAccount;
	}

	public void setBorrowerAccount(String borrowerAccount) {
		this.borrowerAccount = borrowerAccount;
	}

	public String getBorrowerName() {
		return borrowerName;
	}

	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public List<Integer> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<Integer> productTypes) {
		this.productTypes = productTypes;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
}
