package com.fenlibao.p2p.model.xinwang.vo.sign;

/**
 * @author zeronx on 2017/12/29 17:26.
 * @version 1.0
 */
public class SignPersonCaVO {
    private String caType = "CFCA"; // 证书类型 个人为CA_TYPE.CFCA
    private String name; // 用户名称
    private String password; // 用户密码
    private String linkMobile; // 手机号
    private String email; // 个人加密身份证
    private String address; // 地址
    private String province; // 省份
    private String city; // 城市
    private String linkIdCode; // 身分分证

    public SignPersonCaVO() {
    }

    public SignPersonCaVO(String name, String password, String linkMobile, String email, String address, String province, String city, String linkIdCode) {
        this.name = name;
        this.password = password;
        this.linkMobile = linkMobile;
        this.email = email;
        this.address = address;
        this.province = province;
        this.city = city;
        this.linkIdCode = linkIdCode;
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
}
