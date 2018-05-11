package com.fenlibao.p2p.model.xinwang.entity.credit;

import com.fenlibao.p2p.model.xinwang.enums.credit.SysCredit_Transfering;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * t6251
 */
public class SysCredit {
    private Integer id;
    private String code;
    private Integer projectId;
    private Integer creditorId;
    private BigDecimal purchasePrice;
    private BigDecimal originCreditAmount;
    private BigDecimal holdCreditAmount;
    private SysCredit_Transfering transfering;
    private Date createDate;
    private Date bearInterestDate;
    private Integer tenderId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(Integer creditorId) {
        this.creditorId = creditorId;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getOriginCreditAmount() {
        return originCreditAmount;
    }

    public void setOriginCreditAmount(BigDecimal originCreditAmount) {
        this.originCreditAmount = originCreditAmount;
    }

    public BigDecimal getHoldCreditAmount() {
        return holdCreditAmount;
    }

    public void setHoldCreditAmount(BigDecimal holdCreditAmount) {
        this.holdCreditAmount = holdCreditAmount;
    }

    public SysCredit_Transfering getTransfering() {
        return transfering;
    }

    public void setTransfering(SysCredit_Transfering transfering) {
        this.transfering = transfering;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBearInterestDate() {
        return bearInterestDate;
    }

    public void setBearInterestDate(Date bearInterestDate) {
        this.bearInterestDate = bearInterestDate;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public static String creditCode(int tenderId) {
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        return ("Z" + decimalFormat.format(tenderId));
    }
}
