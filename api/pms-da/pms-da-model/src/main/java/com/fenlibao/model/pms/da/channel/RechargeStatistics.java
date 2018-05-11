package com.fenlibao.model.pms.da.channel;

import java.math.BigDecimal;

/**
 * 充值统计
 * Created by chenzhixuan on 2015/11/11.
 */
public class RechargeStatistics {
    private int channelId;// 渠道ID
    private int rechargeCount;// 充值人数
    private BigDecimal rechargeSum;// 充值总额

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(int rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }
}
