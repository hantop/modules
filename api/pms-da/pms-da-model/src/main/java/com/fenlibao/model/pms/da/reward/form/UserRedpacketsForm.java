package com.fenlibao.model.pms.da.reward.form;

import com.fenlibao.model.pms.da.reward.UserRedpackets;

/**
 * Created by Bogle on 2015/11/30.
 */
public class UserRedpacketsForm extends UserRedpackets {

    private byte redType;//类型：1：返现券，2：红包

    private byte rewardType;//奖励类型(1:体验金，2：现金红包，3：返现券)

    private Integer tradeType;// 交易类型

    public byte getRedType() {
        return redType;
    }

    public void setRedType(byte redType) {
        this.redType = redType;
    }

    public byte getRewardType() {
        return rewardType;
    }

    public void setRewardType(byte rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }
}
