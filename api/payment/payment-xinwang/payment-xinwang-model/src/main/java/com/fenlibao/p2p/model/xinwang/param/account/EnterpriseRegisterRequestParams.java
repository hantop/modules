package com.fenlibao.p2p.model.xinwang.param.account;

/**
 * Created by Administrator on 2017/5/10.
 */
public class EnterpriseRegisterRequestParams {
    private Integer userId;  //用户id
    private String enterpriseName;  //企业名称
    private String bankLicense;  //开户银行许可证号
    private String legal;  //法人姓名
    private String idCardType;  //证件类型
    private String legalIdCardNo;  //法人证件号
    private String legalIdCardNoWithStar;  //法人证件号带星
    private String legalIdCardNoEncrypt;  //法人证件号加密
    private String contact;  //企业联系人
    private String contactPhone; //联系人手机号
    private String bankcardNo; //企业对公账户
    private String bankcode; // 银行编码
    private String unifiedCode;  //统一社会信用代码
    private String orgNo; //组织机构代码
    private String businessLicense; //营业执照编号
    private String taxNo; //税务登记号
    private String creditCode; //机构信用代码
    private String uri; //页面回跳 URL
    private String authList;

    private String userRole; //用户角色
    private String enterpriseNo;
    private String auditStatus; //审核状态

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getBankLicense() {
        return bankLicense;
    }

    public void setBankLicense(String bankLicense) {
        this.bankLicense = bankLicense;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getLegalIdCardNo() {
        return legalIdCardNo;
    }

    public void setLegalIdCardNo(String legalIdCardNo) {
        this.legalIdCardNo = legalIdCardNo;
    }

    public String getLegalIdCardNoWithStar() {
        return legalIdCardNoWithStar;
    }

    public void setLegalIdCardNoWithStar(String legalIdCardNoWithStar) {
        this.legalIdCardNoWithStar = legalIdCardNoWithStar;
    }

    public String getLegalIdCardNoEncrypt() {
        return legalIdCardNoEncrypt;
    }

    public void setLegalIdCardNoEncrypt(String legalIdCardNoEncrypt) {
        this.legalIdCardNoEncrypt = legalIdCardNoEncrypt;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getUnifiedCode() {
        return unifiedCode;
    }

    public void setUnifiedCode(String unifiedCode) {
        this.unifiedCode = unifiedCode;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getEnterpriseNo() {
        return enterpriseNo;
    }

    public void setEnterpriseNo(String enterpriseNo) {
        this.enterpriseNo = enterpriseNo;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuthList() {
        return authList;
    }

    public void setAuthList(String authList) {
        this.authList = authList;
    }
}
