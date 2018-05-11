package com.fenlibao.p2p.model.form;

/**
 * 普通注册
 *
 * @author chenzhixuan
 */
public class RegisterForm {

    private String phoneNum;// 注册手机号

    private String password;// 登录密码

    private String spreadPhoneNum;// 推荐人手机号

    private String channelCode;// 渠道编码

    private String verifyCode;// 验证码

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpreadPhoneNum() {
        return spreadPhoneNum;
    }

    public void setSpreadPhoneNum(String spreadPhoneNum) {
        this.spreadPhoneNum = spreadPhoneNum;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

}
