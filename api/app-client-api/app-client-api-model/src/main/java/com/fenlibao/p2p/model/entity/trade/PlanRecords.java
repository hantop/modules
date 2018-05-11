package com.fenlibao.p2p.model.entity.trade;

import java.util.Date;

/**
 * 计划投资记录
 * Created by Administrator on 2017/1/22.
 */
public class PlanRecords {
    private int userId;//用户ID

    private String investorName;//用户名

    private Date investDate;//投资时间

    private double investAmount;//总的投资金额

    private String fullName;//全称

    private long investTime;//投资时间

    public double getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(double investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getInvestDate() {
        return investDate;
    }

    public void setInvestDate(Date investDate) {
        this.investDate = investDate;
    }

    public void setInvestTime(long investTime) {
        this.investTime = investTime;
    }

    public long getInvestTime() {
        return investTime;
    }
}
