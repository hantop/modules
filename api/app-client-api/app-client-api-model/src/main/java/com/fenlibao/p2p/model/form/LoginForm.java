package com.fenlibao.p2p.model.form;

/**
 * 登陆请求参数
 *
 * @author chenzhixuan
 */
public class LoginForm {

    private String username;

    private String password;

    private String phoneNum;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

}
