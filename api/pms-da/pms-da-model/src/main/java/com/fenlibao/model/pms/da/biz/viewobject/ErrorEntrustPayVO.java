package com.fenlibao.model.pms.da.biz.viewobject;


import java.util.Date;

/**
 * 委托支付授权失败
 */
public class ErrorEntrustPayVO {
    private int loanId;// 标的id
    private String bidTitle;// 标的名称
    private String bidNo;// 标的编号
    private String errorReson;// 错误原因
    private Date errorTime;// 报错时间

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getBidTitle() {
        return bidTitle;
    }

    public void setBidTitle(String bidTitle) {
        this.bidTitle = bidTitle;
    }

    public String getBidNo() {
        return bidNo;
    }

    public void setBidNo(String bidNo) {
        this.bidNo = bidNo;
    }

    public String getErrorReson() {
        return errorReson;
    }

    public void setErrorReson(String errorReson) {
        this.errorReson = errorReson;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }
}