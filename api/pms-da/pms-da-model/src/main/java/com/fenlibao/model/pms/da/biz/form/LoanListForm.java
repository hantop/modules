package com.fenlibao.model.pms.da.biz.form;

/**
 * Created by Administrator on 2017/1/17.
 */
public class LoanListForm {
    private String title;// 标的名称
    private String borrowerAccount;// 借款账号
    private String receiptAccount;// 收款账号
    private String status;// 标的状态
    private String orderStatusA;//订单状态1
    private String orderStatusB;//订单状态2    针对订单状态存在待提交/待确认均视为中间状态

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBorrowerAccount() {
        return borrowerAccount;
    }

    public void setBorrowerAccount(String borrowerAccount) {
        this.borrowerAccount = borrowerAccount;
    }

    public String getReceiptAccount() {
        return receiptAccount;
    }

    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderStatusA() {
        return orderStatusA;
    }

    public void setOrderStatusA(String orderStatusA) {
        this.orderStatusA = orderStatusA;
    }

    public String getOrderStatusB() {
        return orderStatusB;
    }

    public void setOrderStatusB(String orderStatusB) {
        this.orderStatusB = orderStatusB;
    }
}
