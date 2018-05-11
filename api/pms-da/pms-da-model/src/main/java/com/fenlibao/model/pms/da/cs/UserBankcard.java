package com.fenlibao.model.pms.da.cs;

import java.util.Date;

/**
 * 用户银行卡
 * Created by chenzhixuan on 2015/12/7.
 */
public class UserBankcard {
    private int userId;// 用户Id
    private String userRole;// 用户角色
    private String bankCardId;// 银行卡记录ID
    private String bankNum;// 银行卡号
    private String bankNumEncrypt;// 银行卡号，加密存储
    private String baofooBindId;//宝付协议号
    private String auditStatus;// 审核状态,针对存管解绑银行卡
    private String unbindStatus;// 解绑状态
    private Date expiryTime;// 可解绑截止时间
    private boolean audit;// 是否可操作审核

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getBankNumEncrypt() {
        return bankNumEncrypt;
    }

    public void setBankNumEncrypt(String bankNumEncrypt) {
        this.bankNumEncrypt = bankNumEncrypt;
    }

    public String getBaofooBindId() {
        return baofooBindId;
    }

    public void setBaofooBindId(String baofooBindId) {
        this.baofooBindId = baofooBindId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getUnbindStatus() {
        return unbindStatus;
    }

    public void setUnbindStatus(String unbindStatus) {
        this.unbindStatus = unbindStatus;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isAudit() {
        return audit;
    }

    public void setAudit(boolean audit) {
        this.audit = audit;
    }
}
