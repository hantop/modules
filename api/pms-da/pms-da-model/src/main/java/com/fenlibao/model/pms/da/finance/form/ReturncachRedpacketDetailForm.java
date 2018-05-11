package com.fenlibao.model.pms.da.finance.form;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ReturncachRedpacketDetailForm {
    private String startDate;
    private String endDate;
    private Integer redpacketId;// 返现券ID
    private boolean systemgrantFlag;// 是否系统自动发放
    private String phoneNum;// 手机号

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

    public Integer getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(Integer redpacketId) {
        this.redpacketId = redpacketId;
    }

    public boolean getSystemgrantFlag() {
        return systemgrantFlag;
    }

    public void setSystemgrantFlag(boolean systemgrantFlag) {
        this.systemgrantFlag = systemgrantFlag;
    }

    public boolean isSystemgrantFlag() {
        return systemgrantFlag;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
