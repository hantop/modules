package com.fenlibao.model.pms.da.channel.form;

/**
 * 渠道统计
 * Created by chenzhixuan on 2015/11/12.
 */
public class ChannelStatisticsForm  {
    private String firstChannelId;
    private String secondChannelId;
    private String startDate;
    private String endDate;
    private String channelName;
    private String channelCode;


    public String getFirstChannelId() {
        return firstChannelId;
    }

    public void setFirstChannelId(String firstChannelId) {
        this.firstChannelId = firstChannelId;
    }

    public String getSecondChannelId() {
        return secondChannelId;
    }

    public void setSecondChannelId(String secondChannelId) {
        this.secondChannelId = secondChannelId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }
}
