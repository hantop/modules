package com.fenlibao.model.pms.da.biz.form;

/**
 * 计划
 * <p>
 * Created by chenzhixuan on 2017/2/7.
 */
public class PlanForm {
    private String title;// 计划名称
    private String releaseStartDate;// 发布开始时间
    private String releaseEndDate;// 发布结束时间
    private String tenderfullStartDate;// 投满开始时间
    private String tenderfullEndDate;// 投满结束时间
    private String status;// 状态

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseStartDate() {
        return releaseStartDate;
    }

    public void setReleaseStartDate(String releaseStartDate) {
        this.releaseStartDate = releaseStartDate;
    }

    public String getReleaseEndDate() {
        return releaseEndDate;
    }

    public void setReleaseEndDate(String releaseEndDate) {
        this.releaseEndDate = releaseEndDate;
    }

    public String getTenderfullStartDate() {
        return tenderfullStartDate;
    }

    public void setTenderfullStartDate(String tenderfullStartDate) {
        this.tenderfullStartDate = tenderfullStartDate;
    }

    public String getTenderfullEndDate() {
        return tenderfullEndDate;
    }

    public void setTenderfullEndDate(String tenderfullEndDate) {
        this.tenderfullEndDate = tenderfullEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
