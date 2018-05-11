package com.fenlibao.p2p.model.entity.channel;

/**
 * 用户来源
 * <p>
 * Created by Administrator on 2016/12/19.
 */
public class UserOrigin {
    private int id;
    private String channelCode;// 渠道编码
    private int userId;// 用户ID
    private int clientTypeId;// 客户端类型ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(int clientTypeId) {
        this.clientTypeId = clientTypeId;
    }
}
