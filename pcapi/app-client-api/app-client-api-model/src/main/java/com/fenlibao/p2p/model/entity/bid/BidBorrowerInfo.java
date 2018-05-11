package com.fenlibao.p2p.model.entity.bid;

/** 
 * @Description: 标对应借款人详细信息
 * @author: junda.feng
 */
public class BidBorrowerInfo{

	private Integer id; //借款人id
	
	private Integer bid; //标id
	
	private Integer user_id; //借款人id
	
	private String borrower_info; //借款人描述信息
	
	private String credit; //征信信息
	
	private String bank_transaction; //银行流水
	
	private String risk; //风控审核项目

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getBorrower_info() {
		return borrower_info;
	}

	public void setBorrower_info(String borrower_info) {
		this.borrower_info = borrower_info;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getBank_transaction() {
		return bank_transaction;
	}

	public void setBank_transaction(String bank_transaction) {
		this.bank_transaction = bank_transaction;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}
	
}
