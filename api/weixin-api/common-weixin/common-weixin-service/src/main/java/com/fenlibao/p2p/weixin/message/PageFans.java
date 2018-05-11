package com.fenlibao.p2p.weixin.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Bogle on 2015/12/7.
 */
public class PageFans extends WxMsg implements Serializable {

    @JsonProperty(value = "total")
    private int total;//	关注该公众账号的总用户数
    @JsonProperty(value = "count")
    private int count;    //拉取的OPENID个数，最大值为10000
    @JsonProperty(value = "data")
    private Openid data;//	列表数据，OPENID的列表
    @JsonProperty(value = "next_openid")
    private String nextpenid;//	拉取列表的最后一个用户的OPENIDO

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Openid getData() {
        return data;
    }

    public void setData(Openid data) {
        this.data = data;
    }

    public String getNextpenid() {
        return nextpenid;
    }

    public void setNextpenid(String nextpenid) {
        this.nextpenid = nextpenid;
    }
}
