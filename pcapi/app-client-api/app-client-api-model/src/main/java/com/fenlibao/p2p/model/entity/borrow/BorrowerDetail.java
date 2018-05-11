package com.fenlibao.p2p.model.entity.borrow;

/**
 * Created by Administrator on 2017/11/20.
 */
public class BorrowerDetail {

    private String name;//借款人姓名

    private String industry;//所属行业

    private String idCard;//证件号

    private String subjectType;//主体性质

    private String address;//地址

    private String income;  //收入情况

    private String creditReport;//征信报告

    private String bankStatement;//银行流水

    private String otherLoan;//其他借款情况

    private String userSituation;//运用情况

    private String operateSituation;//经营状况

    private String repaymentSituation;//还款情况

    private String overdueSituation;//逾期情况

    private String appealSituation;//涉诉情况

    private String punishSituation;//处罚情况

    private String borrowerType;//借款人类型

    private String enterpriseCode;//企业注册号

    private String enterpriseName;//企业名称

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankStatement() {
        return bankStatement;
    }

    public void setBankStatement(String bankStatement) {
        this.bankStatement = bankStatement;
    }

    public String getCreditReport() {
        return creditReport;
    }

    public void setCreditReport(String creditReport) {
        this.creditReport = creditReport;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherLoan() {
        return otherLoan;
    }

    public void setOtherLoan(String otherLoan) {
        this.otherLoan = otherLoan;
    }

    public String getUserSituation() {
        return userSituation;
    }

    public void setUserSituation(String userSituation) {
        this.userSituation = userSituation;
    }

    public String getOperateSituation() {
        return operateSituation;
    }

    public void setOperateSituation(String operateSituation) {
        this.operateSituation = operateSituation;
    }

    public String getAppealSituation() {
        return appealSituation;
    }

    public void setAppealSituation(String appealSituation) {
        this.appealSituation = appealSituation;
    }

    public String getOverdueSituation() {
        return overdueSituation;
    }

    public void setOverdueSituation(String overdueSituation) {
        this.overdueSituation = overdueSituation;
    }

    public String getPunishSituation() {
        return punishSituation;
    }

    public void setPunishSituation(String punishSituation) {
        this.punishSituation = punishSituation;
    }

    public String getRepaymentSituation() {
        return repaymentSituation;
    }

    public void setRepaymentSituation(String repaymentSituation) {
        this.repaymentSituation = repaymentSituation;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }


}
