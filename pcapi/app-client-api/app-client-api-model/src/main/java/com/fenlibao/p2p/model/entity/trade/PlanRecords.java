package com.fenlibao.p2p.model.entity.trade;

import java.util.Date;

/**
 * 计划投资记录
 * Created by Administrator on 2017/1/22.
 */
public class PlanRecords {
    private int userId;//用户ID

    private String investorName;//用户名

    private Date investTime;//投资时间

    private double investAmount;//总的投资金额

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

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
