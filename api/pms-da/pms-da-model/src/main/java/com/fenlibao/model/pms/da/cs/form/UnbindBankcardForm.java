package com.fenlibao.model.pms.da.cs.form;

/**
 * 解绑银行卡信息
 * Created by chenzhixuan on 2015/12/4.
 */
public class UnbindBankcardForm {

    private String userType;// 用户类型
    private Integer userId;// 用户ID
    private String userAccount;// 用户账号
    private String uid;// 唯一识别码,营业执照或统一社会信用代码
    private String encodeUid;// 加密身份证号
    private String bankcardNum;// 银行卡号
    private String userName;// 用户名称

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEncodeUid() {
        return encodeUid;
    }

    public void setEncodeUid(String encodeUid) {
        this.encodeUid = encodeUid;
    }

    public String getBankcardNum() {
        return bankcardNum;
    }

    public void setBankcardNum(String bankcardNum) {
        this.bankcardNum = bankcardNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
