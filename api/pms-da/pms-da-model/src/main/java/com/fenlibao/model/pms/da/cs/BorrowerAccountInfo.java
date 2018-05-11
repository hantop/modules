package com.fenlibao.model.pms.da.cs;

/**
 * 借款人信息
 */
public class BorrowerAccountInfo extends XWAccountInfo{

    private String balance;// 账号余额
    private String amountToBePaid;// 待还金额
    private String numTobePaid;// 待还笔数
    private String overdueAmount;// 逾期金额
    private String overdueNum;// 逾期笔数

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAmountToBePaid() {
        return amountToBePaid;
    }

    public void setAmountToBePaid(String amountToBePaid) {
        this.amountToBePaid = amountToBePaid;
    }

    public String getNumTobePaid() {
        return numTobePaid;
    }

    public void setNumTobePaid(String numTobePaid) {
        this.numTobePaid = numTobePaid;
    }

    public String getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getOverdueNum() {
        return overdueNum;
    }

    public void setOverdueNum(String overdueNum) {
        this.overdueNum = overdueNum;
    }
}
