package com.fenlibao.platform.model.member;

import java.util.Date;

/**
 * Created by Lullaby on 2016/2/19.
 */
public class MemberConsumeRecord {

    private int id;

    private String openid;

    private int mId;

    private double amount;

    private Date createTime;

    private int dId;

    private String posCId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public String getPosCId() {
        return posCId;
    }

    public void setPosCId(String posCId) {
        this.posCId = posCId;
    }

}
