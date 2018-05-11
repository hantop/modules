package com.fenlibao.p2p.model.xinwang.bo;

import java.math.BigDecimal;

/**
 * 投资人信息
 */
public class InvestorBO {
    private Integer userId;
    private String userName;
    private String realName;
    private String idCardNo;
    private BigDecimal amount;

    public InvestorBO() {
    }

    public InvestorBO(Integer userId, String userName, String realName, String idCardNo, BigDecimal amount) {
        this.userId = userId;
        this.userName = userName;
        this.realName = realName;
        this.idCardNo = idCardNo;
        this.amount = amount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
