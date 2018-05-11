package com.fenlibao.p2p.model.vo.plan;

/**
 * 申请退出计划的VO
 *
 * @author Mingway.Xu
 * @date 2017/3/23 11:50
 */
public class QuitPlanInfoVO {
    private int planRecordId;//计划记录ID
    private double creditCapitalAmount;//退出本金
    private double passedEarning;//债权已过天数的收益
    private String assignmentRate;//债权转让费率
    private String assignmentAgreement;//债权转让及受让协议
    private double bidYield;//预期年化收益率
    private int holdDays;//持有天数

    public int getPlanRecordId() {
        return planRecordId;
    }

    public void setPlanRecordId(int planRecordId) {
        this.planRecordId = planRecordId;
    }

    public double getCreditCapitalAmount() {
        return creditCapitalAmount;
    }

    public void setCreditCapitalAmount(double creditCapitalAmount) {
        this.creditCapitalAmount = creditCapitalAmount;
    }

    public double getPassedEarning() {
        return passedEarning;
    }

    public void setPassedEarning(double passedEarning) {
        this.passedEarning = passedEarning;
    }

    public String getAssignmentRate() {
        return assignmentRate;
    }

    public void setAssignmentRate(String assignmentRate) {
        this.assignmentRate = assignmentRate;
    }

    public String getAssignmentAgreement() {
        return assignmentAgreement;
    }

    public void setAssignmentAgreement(String assignmentAgreement) {
        this.assignmentAgreement = assignmentAgreement;
    }

    public double getBidYield() {
        return bidYield;
    }

    public void setBidYield(double bidYield) {
        this.bidYield = bidYield;
    }

    public int getHoldDays() {
        return holdDays;
    }

    public void setHoldDays(int holdDays) {
        this.holdDays = holdDays;
    }
}
