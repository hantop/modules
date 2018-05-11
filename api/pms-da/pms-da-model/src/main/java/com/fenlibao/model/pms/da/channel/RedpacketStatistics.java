package com.fenlibao.model.pms.da.channel;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/11/10.
 */
public class RedpacketStatistics {
    private int channelId;// 渠道ID
    private int activeRedpacketCount;// 激活红包人数
    private BigDecimal activeRedpacketSum;// 激活红包人数

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getActiveRedpacketCount() {
        return activeRedpacketCount;
    }

    public void setActiveRedpacketCount(int activeRedpacketCount) {
        this.activeRedpacketCount = activeRedpacketCount;
    }

    public BigDecimal getActiveRedpacketSum() {
        return activeRedpacketSum;
    }

    public void setActiveRedpacketSum(BigDecimal activeRedpacketSum) {
        this.activeRedpacketSum = activeRedpacketSum;
    }
}
