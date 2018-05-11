package com.fenlibao.model.pms.da.channel;

import java.math.BigDecimal;

/**
 * 投资统计
 * Created by chenzhixuan on 2015/11/11.
 */
public class InvestStatistics {
    private int channelId;// 渠道ID
    private int investCount;// 投资人数
    private BigDecimal investSum;// 投资总额

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public BigDecimal getInvestSum() {
        return investSum;
    }

    public void setInvestSum(BigDecimal investSum) {
        this.investSum = investSum;
    }
}
