package com.fenlibao.p2p.model.xinwang.entity.project;

/**
 * flb.t_xw_tender
 *
 * @date 2017/5/27 10:15
 */
public class XWTenderEntity {
    Integer id;
    Integer bidId;
    Integer tenderId;
    String preTreatRequestNo;
    String investorPlatformUserNo;
    String borrowerPlatformUserNo;
    String makeLoanRequestNo;
    String cancelTenderRequestNo;
    Boolean send;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
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

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }

    public String getCancelTenderRequestNo() {
        return cancelTenderRequestNo;
    }

    public void setCancelTenderRequestNo(String cancelTenderRequestNo) {
        this.cancelTenderRequestNo = cancelTenderRequestNo;
    }
}
