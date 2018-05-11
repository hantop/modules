package com.fenlibao.p2p.sms.config;


import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.sms.config.annotation.PropMap;

/**
 * Created by Administrator on 2015/8/27.
 */
public class EmayInfo {

    public static final String TYPE = "EMAY";

    @PropMap("EMAY_SOFTWARE_SERIAL_NO")
    private String softwareSerialNo;//序列号

    @PropMap("EMAY_PASSWORD")
    private String pwd;//密码（密码请手动输入）

    @PropMap("EMAY_SPECIAL_NO")
    private String specialNo;//特服号（方便后台查询时提供）

    @PropMap("EMAY_KEY")
    private String key;

    @PropMap("EMAY_AUTO_REGISTER")
    private Boolean autoRegister;

    @PropMap("EMAY_SIGN")
    private String sign;//签名

    public String getSoftwareSerialNo() {
        return softwareSerialNo;
    }

    public void setSoftwareSerialNo(String softwareSerialNo) {
        this.softwareSerialNo = softwareSerialNo;
    }

    public String getPwd() {
        return AES.getInstace().decrypt(pwd);
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSpecialNo() {
        return specialNo;
    }

    public void setSpecialNo(String specialNo) {
        this.specialNo = specialNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(Boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
