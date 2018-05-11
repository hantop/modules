package com.fenlibao.p2p.model.form;

/**
 * 银行可信息
 * Created by LouisWang on 2015/8/19.
 */
public class BankCardQuery {
    /**
     * 银行ID
     */
    private int bankId;
    /**
     * 开户行所在地
     */
    private String city;
    /**
     * 开户行
     */
    public String subbranch;
    /**
     * 银行卡号
     */
    public String bankNumber;

    /**
     * 用户ID
     */
    public int acount;

    /**
     * 银行卡状态
     */
    public String status;

    /**
     * 开户名
     */
    public String userName;

    /**
     * 开户类型（区分是个人还是企业）
     */
    public int type;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubbranch() {
        return subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public int getAcount() {
        return acount;
    }

    public void setAcount(int acount) {
        this.acount = acount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
