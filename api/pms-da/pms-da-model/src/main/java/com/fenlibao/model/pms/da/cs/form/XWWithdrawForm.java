package com.fenlibao.model.pms.da.cs.form;

/**
 * Created by zeronx on 2017/6/30.
 */
public class XWWithdrawForm {

    private Integer userId; // 用户id
    private String account; // 用户账号
    private String userType; // 用户类型
    private String name; // 用户名称
    private String bankCode; // 银行编码
    private String amount; // 金额
    private String withdrawType; // 提现方式，NORMAL 表示普通提现，URGENT 表示加急提现
    private String withdrawForm; // 提现类型，IMMEDIATE 为直接提现，CONFIRMED 为待确认提现，
                                 // 不传默认为直接提现方式；待确认提现不支持加急出款，当提现方式为加急提现时，提现类型不支持待确认提现；
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType;
    }

    public String getWithdrawForm() {
        return withdrawForm;
    }

    public void setWithdrawForm(String withdrawForm) {
        this.withdrawForm = withdrawForm;
    }
}
