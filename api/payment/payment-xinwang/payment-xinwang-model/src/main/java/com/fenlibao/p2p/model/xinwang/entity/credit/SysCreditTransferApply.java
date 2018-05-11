package com.fenlibao.p2p.model.xinwang.entity.credit;

import com.fenlibao.p2p.model.xinwang.enums.credit.CreditTransferApply_Status;

import java.math.BigDecimal;
import java.util.Date;

/**
 * t6260
 */
public class SysCreditTransferApply {
    private Integer id;
    private Integer creditId;
    private BigDecimal price;
    private BigDecimal share;
    private Date createTime;
    private Date endDate;
    private CreditTransferApply_Status status;
    private BigDecimal commission;
    private BigDecimal discount;
    private String creditsaleNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CreditTransferApply_Status getStatus() {
        return status;
    }

    public void setStatus(CreditTransferApply_Status status) {
        this.status = status;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getCreditsaleNo() {
        return creditsaleNo;
    }

    public void setCreditsaleNo(String creditsaleNo) {
        this.creditsaleNo = creditsaleNo;
    }
}
