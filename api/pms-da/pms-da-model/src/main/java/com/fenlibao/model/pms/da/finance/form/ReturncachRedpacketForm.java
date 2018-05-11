package com.fenlibao.model.pms.da.finance.form;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ReturncachRedpacketForm {
    private String startDate;
    private String endDate;
    private String activityCode;// 返现券代码
    private boolean systemgrantFlag;// 是否系统自动发放

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public boolean getSystemgrantFlag() {
        return systemgrantFlag;
    }

    public void setSystemgrant(boolean systemgrant) {
        this.systemgrantFlag = systemgrant;
    }
}
