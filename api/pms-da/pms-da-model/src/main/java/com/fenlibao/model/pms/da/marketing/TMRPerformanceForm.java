package com.fenlibao.model.pms.da.marketing;

/**
 * Created by Louis Wang on 2016/3/9.
 */

public class TMRPerformanceForm {

    private String tmrName;// 姓名
    private String fileName;//名单名称
    private String startTime;// 解绑开始时间
    private String endTime;// 解绑结束时间
    private boolean visible; //可见性

    public String getTmrName() {
        return tmrName;
    }

    public void setTmrName(String tmrName) {
        this.tmrName = tmrName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
