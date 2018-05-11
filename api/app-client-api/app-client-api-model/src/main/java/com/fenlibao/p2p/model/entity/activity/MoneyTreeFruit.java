package com.fenlibao.p2p.model.entity.activity;

import java.sql.Timestamp;

/**
 * 查询摇钱树果实实体
 * Created by xiao on 2017/4/24.
 */
public class MoneyTreeFruit {
    int id;//分利果id
    String friendName;//邀请人名称
    Timestamp bornTime;//分利果产生时间
    long leftTime;//分利果离掉落的剩余时间

    public MoneyTreeFruit() {
    }

    public MoneyTreeFruit(int id, String friendName, Timestamp bornTime, long leftTime) {

        this.id = id;
        this.friendName = friendName;
        this.bornTime = bornTime;
        this.leftTime = leftTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public Timestamp getBornTime() {
        return bornTime;
    }

    public void setBornTime(Timestamp bornTime) {
        this.bornTime = bornTime;
    }

    public long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
    }
}
