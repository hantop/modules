package com.fenlibao.model.pms.da.cs.guarantor.form;

/**
 * Created by Administrator on 2017/6/8.
 */
public class GuarantorForm {
    private String account;// 用户账号
    private String name;// 用户名称
    private String userType;// 用户类型 ORGANIZATION:企业、PERSONAL:个人
    private String accountType; // 账号类型 BORROWERS:借款账户、GUARANTEECORP:担保账户
    private String auditStatus; // 开户状态 WAIT:待提交、AUDIT：审核中、BACK:审核回退、REFUSED:审核拒绝、PASSED:审核通过

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
}
