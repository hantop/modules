package com.fenlibao.p2p.model.lottery.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class LotteryPrizeInfo implements Serializable {

    //奖项id
    private int prizeId;

    //奖项名称
    private String prizeName;

    //奖项类型
    private int prizeType;

    //奖项logo
    private String prizeLogo;

    //中奖概率
    private BigDecimal probability;

    //活动id
    private int activityId;

    //显示顺序
    private int listOrder;

    public int getPrizeId() {
        return prizeId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeLogo() {
        return prizeLogo;
    }

    public void setPrizeLogo(String prizeLogo) {
        this.prizeLogo = prizeLogo;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public int getListOrder() {
        return listOrder;
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }
}