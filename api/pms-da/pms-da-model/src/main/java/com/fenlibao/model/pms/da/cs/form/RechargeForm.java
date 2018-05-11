package com.fenlibao.model.pms.da.cs.form;

/**
 * Created by zeronx on 2017/6/30.
 */
public class RechargeForm {

    private Integer userId; // 用户id
    private String account; // 用户账号
    private String userType; // 用户类型
    private String name; // 用户名称
    private String bankCode; // 银行编码
    private String bankcardNo; // 银行卡号
    private String amount; // 金额
    private String rechargeWay; //【支付方式】，支持网银（WEB）、快捷支付（SWIFT）
    private String payType; //【网银类型】，若支付方式填写为网银，且对【银行编码】进行了填写，则此处必填。

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRechargeWay() {
        return rechargeWay;
    }

    public void setRechargeWay(String rechargeWay) {
        this.rechargeWay = rechargeWay;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
