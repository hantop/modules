package com.fenlibao.model.pms.da.channel;

/**
 * Created by Administrator on 2015/11/10.
 */
public class AuthStatistics {
    private int channelId;// 渠道ID
    private int authCount;// 实名认证人数

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getAuthCount() {
        return authCount;
    }

    public void setAuthCount(int authCount) {
        this.authCount = authCount;
    }
}
