package com.fenlibao.p2p.model.vo.supervise;

/**
 * Created by Administrator on 2017/11/21.
 */
public class PlanInfoVO {
    private Object projectName; //项目名称;
    private Object planAmount;  //组合金额;
    private Object planTimeLimit;  //组合期限;
    private Object exceptAnnualised;  //预期年化;
    private Object interestWay;  //计息方式;
    private Object interestTime;  //起息时间;
    private Object earlyQuit;  //提前退出;
    private Object repaymentType;  //还款方式;
    private Object guaranteeMeasure;  //担保措施;


    public Object getProjectName() {
        return projectName;
    }

    public void setProjectName(Object projectName) {
        this.projectName = projectName;
    }

    public Object getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(Object planAmount) {
        this.planAmount = planAmount;
    }

    public Object getPlanTimeLimit() {
        return planTimeLimit;
    }

    public void setPlanTimeLimit(Object planTimeLimit) {
        this.planTimeLimit = planTimeLimit;
    }

    public Object getExceptAnnualised() {
        return exceptAnnualised;
    }

    public void setExceptAnnualised(Object exceptAnnualised) {
        this.exceptAnnualised = exceptAnnualised;
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

    public Object getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Object repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Object getGuaranteeMeasure() {
        return guaranteeMeasure;
    }

    public void setGuaranteeMeasure(Object guaranteeMeasure) {
        this.guaranteeMeasure = guaranteeMeasure;
    }

    @Override
    public String toString() {
        return "PlanInfoVO{" +
                "projectName=" + projectName +
                ", planAmount=" + planAmount +
                ", planTimeLimit=" + planTimeLimit +
                ", exceptAnnualised=" + exceptAnnualised +
                ", interestWay=" + interestWay +
                ", interestTime=" + interestTime +
                ", earlyQuit=" + earlyQuit +
                ", repaymentType=" + repaymentType +
                ", guaranteeMeasure=" + guaranteeMeasure +
                '}';
    }
}
