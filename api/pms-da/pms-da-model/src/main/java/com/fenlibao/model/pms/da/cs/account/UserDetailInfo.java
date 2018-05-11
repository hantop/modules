package com.fenlibao.model.pms.da.cs.account;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户详细信息
 * Created by Administrator on 2015/12/25.
 */
public class UserDetailInfo {
    private int userId;
    private String phoneNum;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date registerTime;
    private String name;// 姓名
    private String idcard;// 身份证号码
    private BigDecimal balance;// 余额
    private BigDecimal investingMoney;// 在投金额
    private String authStatus;// 实名认证状态
    private String bankcardStatus;// 银行卡状态
    private String bankcardNum;// 银行卡号
    private String spreadPhonenum;// 邀请人手机号
    private String spreadName;// 邀请人姓名
    private String spreadAuthStatus;// 邀请人实名认证状态
    private String channelName;// 渠道名称

    private BigDecimal withdrawBlockAmount; // 提现冻结金额
    private BigDecimal investorBlockAmount; // 投资冻结金额
    private BigDecimal interestReceivedAmount; // 已收利息金额
    private BigDecimal interestingAmount; //　待收利息金额

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInvestingMoney() {
        return investingMoney;
    }

    public void setInvestingMoney(BigDecimal investingMoney) {
        this.investingMoney = investingMoney;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getBankcardStatus() {
        return bankcardStatus;
    }

    public void setBankcardStatus(String bankcardStatus) {
        this.bankcardStatus = bankcardStatus;
    }

    public String getBankcardNum() {
        return bankcardNum;
    }

    public void setBankcardNum(String bankcardNum) {
        this.bankcardNum = bankcardNum;
    }

    public String getSpreadPhonenum() {
        return spreadPhonenum;
    }

    public void setSpreadPhonenum(String spreadPhonenum) {
        this.spreadPhonenum = spreadPhonenum;
    }

    public String getSpreadName() {
        return spreadName;
    }

    public void setSpreadName(String spreadName) {
        this.spreadName = spreadName;
    }

    public String getSpreadAuthStatus() {
        return spreadAuthStatus;
    }

    public void setSpreadAuthStatus(String spreadAuthStatus) {
        this.spreadAuthStatus = spreadAuthStatus;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getWithdrawBlockAmount() {
        return withdrawBlockAmount;
    }

    public void setWithdrawBlockAmount(BigDecimal withdrawBlockAmount) {
        this.withdrawBlockAmount = withdrawBlockAmount;
    }

    public BigDecimal getInvestorBlockAmount() {
        return investorBlockAmount;
    }

    public void setInvestorBlockAmount(BigDecimal investorBlockAmount) {
        this.investorBlockAmount = investorBlockAmount;
    }

    public BigDecimal getInterestReceivedAmount() {
        return interestReceivedAmount;
    }

    public void setInterestReceivedAmount(BigDecimal interestReceivedAmount) {
        this.interestReceivedAmount = interestReceivedAmount;
    }

    public BigDecimal getInterestingAmount() {
        return interestingAmount;
    }

    public void setInterestingAmount(BigDecimal interestingAmount) {
        this.interestingAmount = interestingAmount;
    }
}