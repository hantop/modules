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

    //企业注册相关参数
    private String businessLicenseNumber;//营业执照编号
    private String organizingInstitutionBarCode;//组织机构代码
    private String taxRegistrationId;//税务登记号
    private String unifiedSocialCreditIdentifier;//统一社会信用代码

    private String userType; //ZRR.FZRR

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public String getOrganizingInstitutionBarCode() {
        return organizingInstitutionBarCode;
    }

    public void setOrganizingInstitutionBarCode(String organizingInstitutionBarCode) {
        this.organizingInstitutionBarCode = organizingInstitutionBarCode;
    }

    public String getTaxRegistrationId() {
        return taxRegistrationId;
    }

    public void setTaxRegistrationId(String taxRegistrationId) {
        this.taxRegistrationId = taxRegistrationId;
    }

    public String getUnifiedSocialCreditIdentifier() {
        return unifiedSocialCreditIdentifier;
    }

    public void setUnifiedSocialCreditIdentifier(String unifiedSocialCreditIdentifier) {
        this.unifiedSocialCreditIdentifier = unifiedSocialCreditIdentifier;
    }
}
