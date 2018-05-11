package com.fenlibao.p2p.model.xinwang.entity.account;

import java.io.Serializable;

/**
 * @date 2017/6/2 14:18
 */
public class XWBankInfo implements Serializable{
    Integer id;
    String bankName;
    String bankCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
