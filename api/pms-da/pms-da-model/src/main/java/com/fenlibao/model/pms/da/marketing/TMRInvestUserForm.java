package com.fenlibao.model.pms.da.marketing;

/**
 * 用户投资form
 * Created by Louis Wang on 2016/3/11.
 */
public class TMRInvestUserForm {

    private Integer id; //客服导入信息
    private String telPhone;// 电话
    private String startTime;// 解绑开始时间
    private String endTime;// 解绑结束时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
