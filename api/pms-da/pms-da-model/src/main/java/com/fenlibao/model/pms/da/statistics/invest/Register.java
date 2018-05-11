package com.fenlibao.model.pms.da.statistics.invest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Bogle on 2015/12/30.
 */
public class Register implements Serializable {
    private static final long serialVersionUID = 93219182909211010L;

    private Integer userId;//用户id
    private String registerTime;//注册时间
    private String phone;//手机号码
    private String name;// 名称
    private String realName;//实名认证
    private String cardNum;// 身份证号
    private String bindBank;//是否绑卡
    private String clientType;//客户端类型
    private BigDecimal fund;//充值金额
    private BigDecimal investMoney = new BigDecimal(0);//投资金额
    private Integer investNum;//投资次数

    private String channelName; //渠道来源
    private BigDecimal investSum = new BigDecimal(0); //在投金额
    private BigDecimal balance = new BigDecimal(0); //账户余额

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBindBank() {
        return bindBank;
    }

    public void setBindBank(String bindBank) {
        this.bindBank = bindBank;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public BigDecimal getFund() {
        return fund;
    }

    public void setFund(BigDecimal fund) {
        this.fund = fund;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public Integer getInvestNum() {
        return investNum;
    }

    public void setInvestNum(Integer investNum) {
        this.investNum = investNum;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getInvestSum() {
        return investSum;
    }

    public void setInvestSum(BigDecimal investSum) {
        this.investSum = investSum;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
