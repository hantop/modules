package com.fenlibao.model.pms.da.cs.form;

public class BorrowerAccountForm {
    private Integer userId;// 用户ID
    private String account;// 用户账号
    private String name;// 用户名称
    private String userType;// 用户类型
    private String auditStatus;// 用户审核状态
    private String activeStatus;// 用户账号状态(银行存管账号已激活/未激活)

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }
}
