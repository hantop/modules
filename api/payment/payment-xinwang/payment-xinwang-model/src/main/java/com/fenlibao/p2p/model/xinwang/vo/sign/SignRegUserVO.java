package com.fenlibao.p2p.model.xinwang.vo.sign;

/**
 * @author zeronx on 2017/12/29 17:19.
 * @version 1.0
 */
public class SignRegUserVO {
    private Integer userType; // 1:个人 2：企业
    private String email; //
    private String mobile; // 手机号
    private String name; //　用户名称

    public SignRegUserVO() {
    }

    public SignRegUserVO(Integer userType, String email, String mobile, String name) {
        this.userType = userType;
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
}
