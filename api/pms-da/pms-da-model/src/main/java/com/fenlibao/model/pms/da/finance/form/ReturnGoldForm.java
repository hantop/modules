package com.fenlibao.model.pms.da.finance.form;

/**
 * Created by Louis Wang on 2016/1/13.
 */

public class ReturnGoldForm {

    private String goldCode;// 体验金代码
    private String startTime;// 解绑开始时间
    private String endTime;// 解绑结束时间


    public String getGoldCode() {
        return goldCode;
    }

    public void setGoldCode(String goldCode) {
        this.goldCode = goldCode;
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
