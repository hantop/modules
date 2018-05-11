package com.fenlibao.p2p.model.xinwang.entity.project;

import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_Cancel;
import com.fenlibao.p2p.model.xinwang.enums.project.TenderRecord_MakeLoan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * t6250 and flb.t_xw_tender
 */
public class XWTenderRecord {
    private Integer id;
    private Integer projectNo;
    private Integer investorId;
    private BigDecimal amount;
    private BigDecimal share;
    private Date investTime;
    private TenderRecord_Cancel cancel;
    private TenderRecord_MakeLoan makeLoan;
    private String preTreatRequestNo;
    private String investorPlatformUserNo;
    private String borrowerPlatformUserNo;
    private String makeLoanRequestNo;
    private String cancelTenderRequestNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(Integer projectNo) {
        this.projectNo = projectNo;
    }

    public Integer getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Integer investorId) {
        this.investorId = investorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public TenderRecord_Cancel getCancel() {
        return cancel;
    }

    public void setCancel(TenderRecord_Cancel cancel) {
        this.cancel = cancel;
    }

    public TenderRecord_MakeLoan getMakeLoan() {
        return makeLoan;
    }

    public void setMakeLoan(TenderRecord_MakeLoan makeLoan) {
        this.makeLoan = makeLoan;
    }

    public String getPreTreatRequestNo() {
        return preTreatRequestNo;
    }

    public void setPreTreatRequestNo(String preTreatRequestNo) {
        this.preTreatRequestNo = preTreatRequestNo;
    }

    public String getInvestorPlatformUserNo() {
        return investorPlatformUserNo;
    }

    public void setInvestorPlatformUserNo(String investorPlatformUserNo) {
        this.investorPlatformUserNo = investorPlatformUserNo;
    }

    public String getBorrowerPlatformUserNo() {
        return borrowerPlatformUserNo;
    }

    public void setBorrowerPlatformUserNo(String borrowerPlatformUserNo) {
        this.borrowerPlatformUserNo = borrowerPlatformUserNo;
    }

    public String getMakeLoanRequestNo() {
        return makeLoanRequestNo;
    }

    public void setMakeLoanRequestNo(String makeLoanRequestNo) {
        this.makeLoanRequestNo = makeLoanRequestNo;
    }

    public String getCancelTenderRequestNo() {
        return cancelTenderRequestNo;
    }

    public void setCancelTenderRequestNo(String cancelTenderRequestNo) {
        this.cancelTenderRequestNo = cancelTenderRequestNo;
    }
}
