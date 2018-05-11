package com.fenlibao.p2p.model.xinwang.entity.account;

import com.fenlibao.p2p.model.xinwang.enums.account.AuditStatus;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.account.XWActiveStatus;
import com.fenlibao.p2p.model.xinwang.enums.account.XWUserType;

/**
 * Created by Administrator on 2017/5/10.
 */
public class XinwangAccount {
    private Integer id;
    private String platformUserNo;
    private Integer userId;  //t6110.F01
    private UserRole userRole;
    private AuditStatus auditStatus;  //审核状态
    private XWUserType userType;  //用户类型（个人/企业）
    private XWActiveStatus activeStatus;  //用户状态（ACTIVATED：可用，DEACTIVATED：不可用），用户向新网申请清退，新网把状态改为不可用
    private String bankcardNo;  //银行卡号,加密
    private String bankcode;  //银行编码
    private String mobile; //银行预留手机号，企业号可不填
    private String authlist; //用户授权列表，逗号隔开
    private Boolean importUserActivate = true;  //迁移导入会员激活状态
    private Integer bankId; //平台的银行id

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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public XWUserType getUserType() {
        return userType;
    }

    public void setUserType(XWUserType userType) {
        this.userType = userType;
    }

    public XWActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(XWActiveStatus activeStatus) {
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
