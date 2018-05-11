package com.fenlibao.p2p.model.entity.activity;

/**
 * 摇钱树中奖奖品实体
 * Created by xiao on 2017/4/24.
 */
public class MoneyTreePrize {
    int id;//分利果id
    int prizeId;//中奖纪录id
    int prizeType;//奖品类型
    String prizeName;//奖品名称

    public MoneyTreePrize() {
    }

    public MoneyTreePrize(int id, int prizeId, int prizeType, String prizeName) {
        this.id = id;
        this.prizeId = prizeId;
        this.prizeType = prizeType;
        this.prizeName = prizeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
}
