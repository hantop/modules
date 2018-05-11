package com.fenlibao.model.pms.da.cs.guarantor.vo;

/**
 * Created by Administrator on 2017/6/8.
 */
public class GuarantorVO {
    private String account;// 用户账号
    private String name;// 用户名称
    private String userType;// 用户类型

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
}
