package com.fenlibao.p2p.model.lottery.entity;

import java.io.Serializable;
import java.util.Date;

public class UserLotteryDrawPrizeRecord extends UserLotteryRecord implements Serializable {

    Date drawTime; //中奖时间

    public Date getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Date drawTime) {
        this.drawTime = drawTime;
    }
}