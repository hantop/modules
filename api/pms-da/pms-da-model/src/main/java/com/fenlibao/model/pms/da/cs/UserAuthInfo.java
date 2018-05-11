package com.fenlibao.model.pms.da.cs;

/**
 * 用户实名认证信息
 * Created by chenzhixuan on 2015/12/7.
 */
public class UserAuthInfo {
    private Integer userId;// 用户ID
    private String userAccount;// 账号
    private String phoneNum;// 手机号码
    private String idcardEncrypt; //身份证号加密
    private String idcard;//身份证号（未加密）
    private String userName;// 用户名称

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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getIdcardEncrypt() {
        return idcardEncrypt;
    }

    public void setIdcardEncrypt(String idCardEncrypt) {
        this.idcardEncrypt = idCardEncrypt;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idCard) {
        this.idcard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
