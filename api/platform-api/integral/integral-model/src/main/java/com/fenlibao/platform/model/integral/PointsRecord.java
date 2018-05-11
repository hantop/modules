package com.fenlibao.platform.model.integral;

import java.util.Date;

/**
 * Created by Lullaby on 2016/2/19.
 */
public class PointsRecord {

    private int id;

    private int userId;

    private int numbers;

    private byte pointStatus;

    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public byte getPointStatus() {
        return pointStatus;
    }

    public void setPointStatus(byte pointStatus) {
        this.pointStatus = pointStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
