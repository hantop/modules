package com.fenlibao.p2p.model.check.amount;

/**
 * Created by Administrator on 2017/5/10.
 */
public class XwAccount {

    private Integer id;
    private String platformUserNo;
    /**
     * t6110.F01
     */
    private Integer userId;

    private String userRole;
    /**
     * 审核状态
     */
    private String auditStatus;
    /**
     * 用户类型（个人/企业）
     */
    private String userType;

    /**
     * 用户状态（ACTIVATED：可用，DEACTIVATED：不可用），用户向新网申请清退，新网把状态改为不可用
     */
    private String activeStatus;
    /**
     * 银行卡号,加密
     */
    private String bankcardNo;
    /**
     * 银行编码
     */
    private String bankcode;
    /**
     * 银行预留手机号，企业号可不填
     */
    private String mobile;
    /**
     * 用户授权列表，逗号隔开
     */
    private String authlist;
    /**
     * 迁移导入会员激活状态
     */
    private Boolean importUserActivate = true;

    /**
     * 平台的银行id
     */
    private Integer bankId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAuthlist() {
        return authlist;
    }

    public void setAuthlist(String authlist) {
        this.authlist = authlist;
    }

    public Boolean getImportUserActivate() {
        return importUserActivate;
    }

    public void setImportUserActivate(Boolean importUserActivate) {
        this.importUserActivate = importUserActivate;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }
}

