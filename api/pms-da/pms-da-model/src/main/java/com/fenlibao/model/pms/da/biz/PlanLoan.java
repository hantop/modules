package com.fenlibao.model.pms.da.biz;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/13.
 */
public class PlanLoan {
    private int planId;
    private int loanId;
    private String status;
    private Date tenderfullTime;// 投满时间

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTenderfullTime() {
        return tenderfullTime;
    }

    public void setTenderfullTime(Date tenderfullTime) {
        this.tenderfullTime = tenderfullTime;
    }
}
