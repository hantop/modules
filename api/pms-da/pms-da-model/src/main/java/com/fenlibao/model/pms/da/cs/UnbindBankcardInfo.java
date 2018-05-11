package com.fenlibao.model.pms.da.cs;

import java.util.Date;

/**
 * 解绑银行卡信息
 * Created by chenzhixuan on 2015/12/4.
 */
public class UnbindBankcardInfo {
    private int id;
    private String userType;// 用户类型
    private Integer userId;// 用户ID
    private String userAccount;// 用户账号
    private String bankcardNo;// 银行卡号
    private String operator;// 解绑人
    private String description;// 备注
    private Date unbindTime;// 解绑时间
    private String userRole;// 用户角色
    private int unbindStatus;// 解绑状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUnbindTime() {
        return unbindTime;
    }

    public void setUnbindTime(Date unbindTime) {
        this.unbindTime = unbindTime;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getUnbindStatus() {
        return unbindStatus;
    }

    public void setUnbindStatus(int unbindStatus) {
        this.unbindStatus = unbindStatus;
    }
}
