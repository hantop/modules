package com.fenlibao.p2p.model.entity.activity;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/4/26.
 */
public class ActivityEntity {

    private String activityId;//活动id

    private String activityCode;//活动编码

    private String activityTitle;//活动标题\

    private String activityContent;//活动内容

    private String activityUrl;//活动链接

    private String status;//“WKS”未开始，“JXZ”进行中，“YJS”已结束

    private Timestamp startTime;//开始时间

    private Timestamp endTime;//结束时间

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityUrl() {
        return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
        this.activityUrl = activityUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
}
