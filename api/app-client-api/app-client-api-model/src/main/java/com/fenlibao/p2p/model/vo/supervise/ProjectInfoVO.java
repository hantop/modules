package com.fenlibao.p2p.model.vo.supervise;

/**
 * Created by Administrator on 2017/11/21.
 */
public class ProjectInfoVO {
    private Object projectName;  //项目名称;
    private Object borrowAmount;  //借款金额;
    private Object borrowTimeLimit;  //借款期限;
    private Object exceptAnnualised;  //预期年化;
    private Object borrowPurpose;  //借款用途;
    private Object authenticationType;  //认证类型;数据库叫资产类型
    private Object repaymentSource;  //还款来源;
    private Object repaymentType; //还款方式;
    private Object interestWay;  //计息方式;
    private Object interestTime;  //起息时间;
    private Object earlyQuit;  //提前退出;
    private Object guaranteeMeasure;  //担保措施;

    public Object getProjectName() {
        return projectName;
    }

    public void setProjectName(Object projectName) {
        this.projectName = projectName;
    }

    public Object getBorrowAmount() {
        return borrowAmount;
    }

    public void setBorrowAmount(Object borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    public Object getBorrowTimeLimit() {
        return borrowTimeLimit;
    }

    public void setBorrowTimeLimit(Object borrowTimeLimit) {
        this.borrowTimeLimit = borrowTimeLimit;
    }

    public Object getExceptAnnualised() {
        return exceptAnnualised;
    }

    public void setExceptAnnualised(Object exceptAnnualised) {
        this.exceptAnnualised = exceptAnnualised;
    }

    public Object getBorrowPurpose() {
        return borrowPurpose;
    }

    public void setBorrowPurpose(Object borrowPurpose) {
        this.borrowPurpose = borrowPurpose;
    }

    public Object getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(Object authenticationType) {
        this.authenticationType = authenticationType;
    }

    public Object getRepaymentSource() {
        return repaymentSource;
    }

    public void setRepaymentSource(Object repaymentSource) {
        this.repaymentSource = repaymentSource;
    }

    public Object getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Object repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Object getInterestWay() {
        return interestWay;
    }

    public void setInterestWay(Object interestWay) {
        this.interestWay = interestWay;
    }

    public Object getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Object interestTime) {
        this.interestTime = interestTime;
    }

    public Object getEarlyQuit() {
        return earlyQuit;
    }

    public void setEarlyQuit(Object earlyQuit) {
        this.earlyQuit = earlyQuit;
    }

    public Object getGuaranteeMeasure() {
        return guaranteeMeasure;
    }

    public void setGuaranteeMeasure(Object guaranteeMeasure) {
        this.guaranteeMeasure = guaranteeMeasure;
    }

    @Override
    public String toString() {
        return "ProjectInfoVO{" +
                "projectName=" + projectName +
                ", borrowAmount=" + borrowAmount +
                ", borrowTimeLimit=" + borrowTimeLimit +
                ", exceptAnnualised=" + exceptAnnualised +
                ", borrowPurpose=" + borrowPurpose +
                ", authenticationType=" + authenticationType +
                ", repaymentSource=" + repaymentSource +
                ", repaymentType=" + repaymentType +
                ", interestWay=" + interestWay +
                ", interestTime=" + interestTime +
                ", earlyQuit=" + earlyQuit +
                ", guaranteeMeasure=" + guaranteeMeasure +
                '}';
    }
}
