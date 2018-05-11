package com.fenlibao.p2p.model.lottery.entity;

import java.io.Serializable;

/**
 * Created by laubrence on 2016/4/28.
 */
public class UserLotteryTimes implements Serializable {

    //记录id
    private int recordId;

    //用户id
    private int userId;

    //活动id
    private int activityId;

    //抽奖可用次数
    private int availTimes;

    //已使用抽奖次数
    private int usedTimes;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getAvailTimes() {
        return availTimes;
    }

    public void setAvailTimes(int availTimes) {
        this.availTimes = availTimes;
    }

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
    }
}
