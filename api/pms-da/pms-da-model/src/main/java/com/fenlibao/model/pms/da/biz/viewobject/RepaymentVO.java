package com.fenlibao.model.pms.da.biz.viewobject;


import java.util.Date;

/**
 * 还款列表显示
 */
public class RepaymentVO {
    private int bidId;// 标的id
    private int borrowUserId;// 借款用户Id
    private String borrowUserAccount;// 借款用户账号
    private String borrowUserName;// 借款用户名
    private int receiptID;// 收款用户Id
    private String receiptAccount;// 收款账号
    private String receiptName;// 收款用户
    private String title;// 标的名称
    private String repayMoney;// 还款金额,实际借款金额
    private int distanceRefund;// 距离还款日
    private Date repayDay;// 还款日
    private int currentTerm;// 当前期数
    private int totalTerm;// 总期数
    private String repayMethod;// 还款方式
    private String termDisplay;// 当前期/总期数
    private int repaying;//是否正在还款中
    private boolean prerepay;// 是否可以提前还款(根据还款日与当前时间判断)
    private String status;// 标的状态
    private String orderState;// 订单状态

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getBorrowUserId() {
        return borrowUserId;
    }

    public void setBorrowUserId(int borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    public String getBorrowUserAccount() {
        return borrowUserAccount;
    }

    public void setBorrowUserAccount(String borrowUserAccount) {
        this.borrowUserAccount = borrowUserAccount;
    }

    public String getBorrowUserName() {
        return borrowUserName;
    }

    public void setBorrowUserName(String borrowUserName) {
        this.borrowUserName = borrowUserName;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public String getReceiptAccount() {
        return receiptAccount;
    }

    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(String repayMoney) {
        this.repayMoney = repayMoney;
    }

    public int getDistanceRefund() {
        return distanceRefund;
    }

    public void setDistanceRefund(int distanceRefund) {
        this.distanceRefund = distanceRefund;
    }

    public Date getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(Date repayDay) {
        this.repayDay = repayDay;
    }

    public int getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(int currentTerm) {
        this.currentTerm = currentTerm;
    }

    public int getTotalTerm() {
        return totalTerm;
    }

    public void setTotalTerm(int totalTerm) {
        this.totalTerm = totalTerm;
    }

    public String getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(String repayMethod) {
        this.repayMethod = repayMethod;
    }

    public String getTermDisplay() {
        return termDisplay;
    }

    public void setTermDisplay(String termDisplay) {
        this.termDisplay = termDisplay;
    }

    public int getRepaying() {
        return repaying;
    }

    public void setRepaying(int repaying) {
        this.repaying = repaying;
    }

    public boolean isPrerepay() {
        return prerepay;
    }

    public void setPrerepay(boolean prerepay) {
        this.prerepay = prerepay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
}