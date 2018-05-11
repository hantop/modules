package com.fenlibao.p2p.model.dm.message.header;

/**
 * Created by zcai on 2016/9/1.
 */
public abstract class HXHeader {

    protected String channelCode; //接入渠道,由华兴提供，华兴发起为 GHB
    protected String channelFlow; //渠道流水号,28位,【格式：渠道标识+YYYYMMDD+所发交易的“交易码”的最后三位+11位商户流水)保证流水的唯一性】由数字、字母（字母区分大小写）
    protected String encryptData; //加密域

    public HXHeader() {}

    public HXHeader(String channelCode, String channelFlow, String encryptData) {
        this.channelCode = channelCode;
        this.channelFlow = channelFlow;
        this.encryptData = encryptData;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelFlow() {
        return channelFlow;
    }

    public void setChannelFlow(String channelFlow) {
        this.channelFlow = channelFlow;
    }

    public String getEncryptData() {
        return encryptData;
    }

    public void setEncryptData(String encryptData) {
        this.encryptData = encryptData;
    }
}
