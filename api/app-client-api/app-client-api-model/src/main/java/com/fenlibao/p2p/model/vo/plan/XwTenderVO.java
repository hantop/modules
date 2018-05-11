package com.fenlibao.p2p.model.vo.plan;

/**
 * @author zeronx on 2017/11/23 9:35.
 * @version 1.0
 */
public class XwTenderVO {

    private Integer bidId; // 标id
    private Integer tenderId; // 投标记录Id t6250.F01
    private String preRequestNo; // 预处理新网流程号
    private String investorUserNo; // 投资者新网编号
    private String borrowerUserNo; // 借款人新网编号

    public XwTenderVO() {
    }

    public XwTenderVO(Integer bidId, Integer tenderId, String preRequestNo, String investorUserNo, String borrowerUserNo) {
        this.bidId = bidId;
        this.tenderId = tenderId;
        this.preRequestNo = preRequestNo;
        this.investorUserNo = investorUserNo;
        this.borrowerUserNo = borrowerUserNo;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public String getPreRequestNo() {
        return preRequestNo;
    }

    public void setPreRequestNo(String preRequestNo) {
        this.preRequestNo = preRequestNo;
    }

    public String getInvestorUserNo() {
        return investorUserNo;
    }

    public void setInvestorUserNo(String investorUserNo) {
        this.investorUserNo = investorUserNo;
    }

    public String getBorrowerUserNo() {
        return borrowerUserNo;
    }

    public void setBorrowerUserNo(String borrowerUserNo) {
        this.borrowerUserNo = borrowerUserNo;
    }
}
