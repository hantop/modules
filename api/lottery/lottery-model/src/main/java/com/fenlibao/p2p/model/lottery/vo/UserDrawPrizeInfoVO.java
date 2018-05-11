package com.fenlibao.p2p.model.lottery.vo;

/**
 * Created by Administrator on 2016/6/16.
 */
public class UserDrawPrizeInfoVO {

    String prizeName; //奖品名称

    long drawTime; //中奖时间

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public long getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(long drawTime) {
        this.drawTime = drawTime;
    }

}
