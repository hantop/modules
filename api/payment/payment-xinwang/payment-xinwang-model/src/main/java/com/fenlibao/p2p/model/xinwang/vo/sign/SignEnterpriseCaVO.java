package com.fenlibao.p2p.model.xinwang.vo.sign;

/**
 * @author zeronx on 2017/12/29 17:26.
 * @version 1.0
 */
public class SignEnterpriseCaVO {
    private String caType = "ZJCA"; // 证书类型 企业为 CA_TYPE.ZJCA;
    private String name; // 企业名称
    private String password; // 用户密码
    private String linkMan; // 企业联系人
    private String linkMobile; // 手机号
    private String email; // //注册号(企业用户必填)，如果为三证合一，填统一社会信用代码
    private String address; // 地址
    private String province; // 省份
    private String city; // 城市
    private String linkIdCode; // 身分分证
    private String icCode;//注册号(企业用户必填)，如果为三证合一，填统一社会信用代码
    private String orgCode = ""; // 组织机构代码(企业用户必填)
    private String taxCode = ""; // 税务登记证号(企业用户必填)

    public SignEnterpriseCaVO() {
    }

    public SignEnterpriseCaVO(String name, String password, String linkMan, String linkMobile, String email, String address, String province, String city, String linkIdCode, String icCode, String orgCode, String taxCode) {
        this.name = name;
        this.password = password;
        this.linkMan = linkMan;
        this.linkMobile = linkMobile;
        this.email = email;
        this.address = address;
        this.province = province;
        this.city = city;
        this.linkIdCode = linkIdCode;
        this.icCode = icCode;
        this.orgCode = orgCode;
        this.taxCode = taxCode;
    }

    public String getCaType() {
        return caType;
    }

    public void setCaType(String caType) {
        this.caType = caType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkMobile() {
        return linkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLinkIdCode() {
        return linkIdCode;
    }

    public void setLinkIdCode(String linkIdCode) {
        this.linkIdCode = linkIdCode;
    }

    public String getIcCode() {
        return icCode;
    }

    public void setIcCode(String icCode) {
        this.icCode = icCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}
