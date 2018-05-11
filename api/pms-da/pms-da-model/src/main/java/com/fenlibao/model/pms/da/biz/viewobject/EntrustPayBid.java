package com.fenlibao.model.pms.da.biz.viewobject;


/**
 * 委托支付标
 */
public class EntrustPayBid extends BidVO {
    private String requestNo;// 请求流水号
    private String userType;// 受托方类型用户类型（个人/企业）
    private String entrustedPlatformUserNo;// 受托方平台用户编号
    private String borrowerPlatformUserNo;// 存管借款用户编号

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEntrustedPlatformUserNo() {
        return entrustedPlatformUserNo;
    }

    public void setEntrustedPlatformUserNo(String entrustedPlatformUserNo) {
        this.entrustedPlatformUserNo = entrustedPlatformUserNo;
    }

    public String getBorrowerPlatformUserNo() {
        return borrowerPlatformUserNo;
    }

    public void setBorrowerPlatformUserNo(String borrowerPlatformUserNo) {
        this.borrowerPlatformUserNo = borrowerPlatformUserNo;
    }
}