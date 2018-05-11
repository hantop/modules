package com.fenlibao.p2p.util.bean;

/**
 * 上上签注册用户 by kris
 */
public class ShangshangqianRegisterUser {
    private Integer userType = 1;  //1个人，2企业
    private String email;
    private String mobile;
    private String name;
    private Integer userId;

    public ShangshangqianRegisterUser() {
    }

    public ShangshangqianRegisterUser(Integer userType, String email, String mobile, String name) {
        this.userType = userType == null ? 1 : userType;
        this.email = email;
        this.mobile = mobile;
        this.name = name;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
