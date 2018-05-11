package com.fenlibao.p2p.model.vo;

import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.entity.UserInfo;

import java.util.List;

/**
 * 用户账户信息VO
 * 
 * @author chenzhixuan
 *
 */
public class UserAccountInfoVO extends UserInfo {
	private static final long serialVersionUID = 1L;

	private double hqbTotal;// 用户活期宝投资总额(1.0版本不做)
	private double kdbTotal;// 用户开店宝的债权总额
	private String ticket;// 卡包券(1.0版本不做)
	private int ticketNum;// 卡包券数量
	private double ticketTotal;// 卡包券总额
	private double friReward;// 邀请好友奖励数额(1.0版本不做)
	private int noDealpasswordFlag;// 免密投标开启状态(0:关闭 1:开启)
	private String dealpasswordFlag;// 是否设置了交易密码(0:没有  1:有)
//	private int autoBid;// 自动投标开启状态(0:关闭 1:开启)
	private String identityName; // 绑定的身份证姓名
	private String identitycard; // 绑定的身份证
	private AccountCheckStatusVO accountCheckStatus;// 账户绑定状态
	private List<BankCard> bankCards;// 绑定的银行卡
	private String token;

	public double getHqbTotal() {
		return hqbTotal;
	}

	public void setHqbTotal(double hqbTotal) {
		this.hqbTotal = hqbTotal;
	}

	public double getKdbTotal() {
		return kdbTotal;
	}

	public void setKdbTotal(double kdbTotal) {
		this.kdbTotal = kdbTotal;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(int ticketNum) {
		this.ticketNum = ticketNum;
	}

	public double getTicketTotal() {
		return ticketTotal;
	}

	public void setTicketTotal(double ticketTotal) {
		this.ticketTotal = ticketTotal;
	}

	public double getFriReward() {
		return friReward;
	}

	public void setFriReward(double friReward) {
		this.friReward = friReward;
	}

	public int getNoDealpasswordFlag() {
		return noDealpasswordFlag;
	}

	public void setNoDealpasswordFlag(int noDealpasswordFlag) {
		this.noDealpasswordFlag = noDealpasswordFlag;
	}

	public String getDealpasswordFlag() {
		return dealpasswordFlag;
	}

	public void setDealpasswordFlag(String dealpasswordFlag) {
		this.dealpasswordFlag = dealpasswordFlag;
	}

	public String getIdentityName() {
		return identityName;
	}

	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}

	public String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}

	public AccountCheckStatusVO getAccountCheckStatus() {
		return accountCheckStatus;
	}

	public void setAccountCheckStatus(AccountCheckStatusVO accountCheckStatus) {
		this.accountCheckStatus = accountCheckStatus;
	}

	public List<BankCard> getBankCards() {
		return bankCards;
	}

	public void setBankCards(List<BankCard> bankCards) {
		this.bankCards = bankCards;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
