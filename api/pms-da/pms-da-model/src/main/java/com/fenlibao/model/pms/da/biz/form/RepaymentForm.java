package com.fenlibao.model.pms.da.biz.form;

/**
 * 还款管理
 * <p>
 */
public class RepaymentForm {

    private int loanId;// 标id
    private String title; //借款名称
    private String borrowUserAccount; //借款账号
    private String receiptAccount; //收款账号
    private String repayDayStart; // 还款开始时间
    private String repayDayEnd; // 还款结束时间

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBorrowUserAccount() {
        return borrowUserAccount;
    }

    public void setBorrowUserAccount(String borrowUserAccount) {
        this.borrowUserAccount = borrowUserAccount;
    }

    public String getReceiptAccount() {
        return receiptAccount;
    }

    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount;
    }

    public String getRepayDayStart() {
        return repayDayStart;
    }

    public void setRepayDayStart(String repayDayStart) {
        this.repayDayStart = repayDayStart;
    }

    public String getRepayDayEnd() {
        return repayDayEnd;
    }

    public void setRepayDayEnd(String repayDayEnd) {
        this.repayDayEnd = repayDayEnd;
    }
}