package com.fenlibao.model.pms.da.biz.form;

/**
 * 发标管理
 * <p>
 */
public class LoanManageForm {

    private int loanId;// 标id
    private String contractNO;// 合同编号
    private String title; //借款标题
    private String account; //借款账户
    private String createTimeStart; // 处理开始时间
    private String createTimeStartWork; // 处理开始时间
    private String createTimeEnd; // 处理结束时间
    private String createTimeEndWork; // 处理结束时间
    private String bidType;// 借款类型
    private String userType;// 账户类型
    private String status;// 状态

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getContractNO() {
        return contractNO;
    }

    public void setContractNO(String contractNO) {
        this.contractNO = contractNO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTimeEndWork() {
        return createTimeEndWork;
    }

    public void setCreateTimeEndWork(String createTimeEndWork) {
        this.createTimeEndWork = createTimeEndWork;
    }

    public String getCreateTimeStartWork() {
        return createTimeStartWork;
    }

    public void setCreateTimeStartWork(String createTimeStartWork) {
        this.createTimeStartWork = createTimeStartWork;
    }
}