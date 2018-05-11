package com.fenlibao.p2p.model.vo;

import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.entity.UserInfo;

import java.util.List;

public class UserInfoResultVO extends UserInfo {

	private static final long serialVersionUID = 1L;

	private AccountCheckStatusVO accountCheckStatusVO;// 账户绑定状态

	private List<BankCard> bankCardList;// 绑定的银行卡

	public AccountCheckStatusVO getAccountCheckStatusVO() {
		return accountCheckStatusVO;
	}

	public void setAccountCheckStatusVO(
			AccountCheckStatusVO accountCheckStatusVO) {
		this.accountCheckStatusVO = accountCheckStatusVO;
	}

	public List<BankCard> getBankCardList() {
		return bankCardList;
	}

	public void setBankCardList(List<BankCard> bankCardList) {
		this.bankCardList = bankCardList;
	}

}
