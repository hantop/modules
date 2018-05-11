package com.fenlibao.p2p.model.dm.message.header;

/**
 * 请求报文header封装
 * Created by zcai on 2016/8/26.
 */
public class RequestHeader extends HXHeader {

    private String channelDate; //渠道日期
    private String channelTime; //渠道时间,注意日期为实际交易的北京标准时间的日期

    public RequestHeader() {}

    /**
     * @param channelCode
     * @param channelFlow
     * @param encryptData
     * @param channelDate
     * @param channelTime
     */
    public RequestHeader(String channelCode, String channelFlow,
                         String channelDate, String channelTime, String encryptData) {
        super(channelCode, channelFlow, encryptData);
        this.channelDate = channelDate;
        this.channelTime = channelTime;
    }

    public String getChannelDate() {
        return channelDate;
    }

    public void setChannelDate(String channelDate) {
        this.channelDate = channelDate;
    }

    public String getChannelTime() {
        return channelTime;
    }

    public void setChannelTime(String channelTime) {
        this.channelTime = channelTime;
    }

}
