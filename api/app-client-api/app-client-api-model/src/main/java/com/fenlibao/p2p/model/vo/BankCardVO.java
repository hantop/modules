package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 银行卡信息(T6114)
 * Created by LouisWang on 2015/8/15.
 */
public class BankCardVO implements Serializable {
    /**
     * id
     */
    public Integer id;
    /**
     * 用户ID
     */
    public Integer acount; //应该是account吧！！！！！！！！！！
    /**
     * 银行ID
     */
    public Integer bankID;
    /**
     * 银行名称
     */
    public String bankname;
    /**
     * 开户行地址
     */
    public String city; //这里是支行所在市的ID
    /**
     * 开户行名称
     */
    public String bankKhhName;

    /**
     * 银行卡号
     */
    public String bankNumber;
    /**
     * 银行卡状态
     */
    public String status;
    /**
     * 银行卡绑定状态
     */
    private String bindStatus;
    /**
     * 开户名
     */
    private String accountName;
    /**
     * 银行卡号（有星号）
     */
    private String bankNoStar;

    public Integer type;
    
    public BankCardVO() {
		super();
	}

	public BankCardVO(String bindStatus, int userId) {
		super();
		this.bindStatus = bindStatus;
		this.acount = userId;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAcount() {
        return acount;
    }

    public void setAcount(Integer acount) {
        this.acount = acount;
    }

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBankKhhName() {
        return bankKhhName;
    }

    public void setBankKhhName(String bankKhhName) {
        this.bankKhhName = bankKhhName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankNoStar() {
        return bankNoStar;
    }

    public void setBankNoStar(String bankNoStar) {
        this.bankNoStar = bankNoStar;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
