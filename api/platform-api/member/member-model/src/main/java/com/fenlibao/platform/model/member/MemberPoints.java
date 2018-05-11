package com.fenlibao.platform.model.member;

import java.util.Date;

/**
 * Created by Lullaby on 2016/2/18.
 */
public class MemberPoints {

    private int id;

    private int tId;

    private Integer userId;

    private int numbers;

    private byte changeType;

    private Date createTime;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public byte getChangeType() {
        return changeType;
    }

    public void setChangeType(byte changeType) {
        this.changeType = changeType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
