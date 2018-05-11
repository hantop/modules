package com.fenlibao.p2p.model.vo.bidinfo;

import com.fenlibao.p2p.model.entity.bid.BidBorrowerInfo;

/** 
 * @Description: 标对应借款人详细信息
 * @author: junda.feng
 */
public class BidBorrowerInfoVO{

	
	private String base; //借款人描述信息
	
	private String credit; //征信信息
	
	private String bankTransaction; //银行流水
	
	private String risk=""; //风控审核项目
	
	private boolean historyInfoFlag;//不是2.0之前的数据

	public BidBorrowerInfoVO(BidBorrowerInfo info) {
		this.base=info.getBorrower_info();
		this.credit=info.getCredit();
		this.bankTransaction=info.getBank_transaction();
		this.risk=info.getRisk();
		this.historyInfoFlag=false;
	}
	
	public boolean isHistoryInfoFlag() {
		return historyInfoFlag;
	}

	public void setHistoryInfoFlag(boolean historyInfoFlag) {
		this.historyInfoFlag = historyInfoFlag;
	}
	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getBankTransaction() {
		return bankTransaction;
	}

	public void setBankTransaction(String bankTransaction) {
		this.bankTransaction = bankTransaction;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	
}
