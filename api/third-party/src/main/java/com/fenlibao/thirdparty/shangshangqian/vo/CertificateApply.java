package com.fenlibao.thirdparty.shangshangqian.vo;

import cn.bestsign.sdk.integration.Constants;

import java.util.Map;

/**
 * 用于CA申请
 */
public class CertificateApply {
    private Constants.CA_TYPE caType = Constants.CA_TYPE.CFCA;
    private String name;
    private String password;
    private String linkMobile;
    private String email;//用phone代替
    private String address;
    private String province;
    private String city;
    private String linkIdCode;//身份证
    public CertificateApply(){

    }
    public CertificateApply(Map map){
        this.name= (String) map.get("name");
        this.password= (String) map.get("password");
        this.linkMobile= (String) map.get("phone");
//        this.email= (String) map.get("email");
        this.address= (String) map.get("address");
        this.province= (String) map.get("province");
        this.city= (String) map.get("city");
        this.linkIdCode= (String) map.get("linkIdCode");
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

    public Constants.CA_TYPE getCaType() {
        return caType;
    }

    public void setCaType(Constants.CA_TYPE caType) {
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

    public String getLinkIdCode() {
        return linkIdCode;
    }

    public void setLinkIdCode(String linkIdCode) {
        this.linkIdCode = linkIdCode;
    }
}
