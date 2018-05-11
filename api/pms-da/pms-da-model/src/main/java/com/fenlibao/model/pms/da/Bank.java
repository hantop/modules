package com.fenlibao.model.pms.da;

/**
 * 银行卡
 *
 * Created by Administrator on 2017/6/29.
 */
public class Bank {

    private int id;//银行自增ID

    private String bankName;//银行名称

    private String status;//状态,QY:启用;TY:停用

    private String code;//代码

    private String lianlianCode;//连连支付银行编码

    private String baofuCode;//宝付银行编码

    private String xinwangCode;//新网银行编码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLianlianCode() {
        return lianlianCode;
    }

    public void setLianlianCode(String lianlianCode) {
        this.lianlianCode = lianlianCode;
    }

    public String getBaofuCode() {
        return baofuCode;
    }

    public void setBaofuCode(String baofuCode) {
        this.baofuCode = baofuCode;
    }

    public String getXinwangCode() {
        return xinwangCode;
    }

    public void setXinwangCode(String xinwangCode) {
        this.xinwangCode = xinwangCode;
    }
}
