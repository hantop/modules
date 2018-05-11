package com.fenlibao.p2p.model.entity.finacing;

/**
 * Created by Administrator on 2017/2/20.
 */
public class PlanCreditInfo {

    private int planRecordId;//计划记录id

    private int creditId;//对应bid的债权id

    private double interestRise;//加息券加息利率

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public double getInterestRise() {
        return interestRise;
    }

    public void setInterestRise(double interestRise) {
        this.interestRise = interestRise;
    }

    public int getPlanRecordId() {
        return planRecordId;
    }

    public void setPlanRecordId(int planRecordId) {
        this.planRecordId = planRecordId;
    }
}
