package com.fenlibao.model.pms.da.cs.form;

/**
 * Created by zeronx on 2017/6/26.
 */
public class GuaranteeForm {
    private Integer userId;// 用户ID
    private String account;// 用户账号
    private String name;// 用户名称
    private String userType;// 用户类型 ORGANIZATION:企业、PERSONAL:个人

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
}
