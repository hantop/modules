package com.fenlibao.p2p.model.vo;

import com.fenlibao.p2p.model.entity.BankCard;
import com.fenlibao.p2p.model.entity.UserInfo;

import java.util.Date;
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
	private String identitycard2; // 绑定的身份证
	private AccountCheckStatusVO accountCheckStatus;// 账户绑定状态
	private List<BankCard> bankCards;// 绑定的银行卡
	private String token;
	
	//增加字段   2016-5-11 junda.feng
	private Date birthday;//出生日期
	private String companyIndustry;//公司行业
	private String companySize;//公司规模
	private String schoole;//学校
	private String income;//收入
	private String sex;//性别
	private String position;//职位

	private String isCgAccount;//是否开通存管(0:否 1：是)

	private String isBinkCards;//存管账户是否绑卡

	private String userType;//个人 ： ZRR  企业 ：FZRR

	private Date registerTime;//注册时间

	

	public String getIdentitycard2() {
		return identitycard2;
	}

	public void setIdentitycard2(String identitycard2) {
		this.identitycard2 = identitycard2;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	

	public String getCompanyIndustry() {
		return companyIndustry;
	}

	public void setCompanyIndustry(String companyIndustry) {
		this.companyIndustry = companyIndustry;
	}

	public String getCompanySize() {
		return companySize;
	}

	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}

	public String getSchoole() {
		return schoole;
	}

	public void setSchoole(String schoole) {
		this.schoole = schoole;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public String getIsCgAccount() {
		return isCgAccount;
	}

	public void setIsCgAccount(String isCgAccount) {
		this.isCgAccount = isCgAccount;
	}

	public String getIsBinkCards() {
		return isBinkCards;
	}

	public void setIsBinkCards(String isBinkCards) {
		this.isBinkCards = isBinkCards;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Override
	public String getUserType() {
		return userType;
	}

	@Override
	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public Date getRegisterTime() {
		return registerTime;
	}

	@Override
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
}
