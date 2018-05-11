package com.fenlibao.thirdparty.shangshangqian.vo;

import cn.bestsign.sdk.integration.Constants;

import java.util.Map;

/**
 * 上上签注册用户 by kris
 */
public class RegisterUser {
    private Constants.USER_TYPE userType = Constants.USER_TYPE.PERSONAL;
    private String email;
    private String mobile;
    private String name;
    public RegisterUser(){}
    public RegisterUser(Map map){
        this.userType= map.get("userType")==null?Constants.USER_TYPE.PERSONAL: (Constants.USER_TYPE) map.get("userType");
        this.mobile= (String) map.get("phone");
        this.name= (String) map.get("name");
    }

    public Constants.USER_TYPE getUserType() {
        return userType;
    }

    public void setUserType(Constants.USER_TYPE userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

//    public void setEmail(String email) {
//        this.email = email;
//    }

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
}
