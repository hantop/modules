package com.fenlibao.model.pms.da.channel;

/**
 * Created by Administrator on 2015/11/10.
 */
public class RegisterStatistics {
    private int channelId;// 渠道ID
    private int registerCount;// 注册人数

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }
}
