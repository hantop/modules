package com.fenlibao.p2p.model.vo.redpacket;

import java.util.Date;

/**
 * 8月奥运红包
 * Created by zcai on 2016/8/3.
 */
public class RedPacketActivityVO {

    private String id;//红包ID
    private String amount; //红包金额
    private String investAmount; //起投金额
    private String investDeadline;//投资期限
    private String isReceive = "0";//是否已经领取
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间
    private int activityStatus = 0;//活动状态(-1=未开始0=进行中1=已结束)

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestDeadline() {
        return investDeadline;
    }

    public void setInvestDeadline(String investDeadline) {
        this.investDeadline = investDeadline;
    }

    public String getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(String isReceive) {
        this.isReceive = isReceive;
    }
}
