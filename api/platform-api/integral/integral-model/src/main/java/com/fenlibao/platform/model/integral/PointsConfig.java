package com.fenlibao.platform.model.integral;

import java.util.Date;

/**
 * 消费金额兑换积分规则配置
 * Created by Lullaby on 2016/2/19.
 */
public class PointsConfig {

    private int id;

    private int tId;

    private short exchangePoint;

    private Date startTime;

    private Date endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public short getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(short exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

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

}
