package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 *  银行卡编码(T5020)
 * Created by LouisWang on 2015/8/15.
 */
public class BankVO implements Serializable {

    /**
     * id = F01
     */
    public int id;
    /**
     * 银行名称 = F02
     */
    public String bankname;
    /**
     * 银行卡状态 = F03
     */
    public String status;
    /**
     * 银行英文简称 = F04
     */
    public String engCode;
    /**
     * 连连支付银行编码 = F05
     */
    public String llCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEngCode() {
        return engCode;
    }

    public void setEngCode(String engCode) {
        this.engCode = engCode;
    }

    public String getLlCode() {
        return llCode;
    }

    public void setLlCode(String llCode) {
        this.llCode = llCode;
    }
}
