package com.fenlibao.model.pms.da.cs;

/**
 * 企业借款人企业信息
 */
public class BussinessInfo{

    private Integer userId;// 用户Id
    private String businessName;// 企业名称
    private String businessNo; // 企业编号, 系统生成
    private String uniformSocialCreditCode;// 统一社会信用代码
    private String businessLicenseNumber;// 营业执照编号
    private String taxID;// 税务登记号
    private String organizationCode;// 组织机构代码
    private String creditCode;// 机构信用代码
    private String corporateJurisdicalPersonalName;// 法人姓名
    private String identificationType; // 证件类型 PRC_ID：中华人民共和国居民身份证，PASSPORT：护照，COMPATRIOTS_CARD：港澳台同胞回乡证，PERMANENT_RESIDENCE：外国人永久居留证
    private String identification;// 法人证件号
    private String linkman;// 企业联系人
    private String phone;// 联系人手机号
    private String bankCode; // 银行编码
    private String bankLicenseNumber;// 开户银行许可证号
    private String publicAccount;// 企业对公账户

    private String auditStatus; // 如果是新添加的担保企业账号 设置为 WAIT：待提交
    private String identificationAsterisk; // 身份证9-16位星号替换

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getUniformSocialCreditCode() {
        return uniformSocialCreditCode;
    }

    public void setUniformSocialCreditCode(String uniformSocialCreditCode) {
        this.uniformSocialCreditCode = uniformSocialCreditCode;
    }

    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getCorporateJurisdicalPersonalName() {
        return corporateJurisdicalPersonalName;
    }

    public void setCorporateJurisdicalPersonalName(String corporateJurisdicalPersonalName) {
        this.corporateJurisdicalPersonalName = corporateJurisdicalPersonalName;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankLicenseNumber() {
        return bankLicenseNumber;
    }

    public void setBankLicenseNumber(String bankLicenseNumber) {
        this.bankLicenseNumber = bankLicenseNumber;
    }

    public String getPublicAccount() {
        return publicAccount;
    }

    public void setPublicAccount(String publicAccount) {
        this.publicAccount = publicAccount;
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

    public String getIdentificationAsterisk() {
        return identificationAsterisk;
    }

    public void setIdentificationAsterisk(String identificationAsterisk) {
        this.identificationAsterisk = identificationAsterisk;
    }
}
