package com.fenlibao.model.pms.common.publicity;

import java.util.Date;

/**
 * 广告图
 *
 * Created by chenzhixuan on 2016/5/11.
 */
public class AdvertImage {
    private int id;
    private String name;// 名称
    private byte priority;// 优先级
    private byte advertType;// 广告类型(对应枚举key)
    private String adWebUrl;// 广告链接
    private byte status;// 状态(缺省0,1:启用,2:禁用)
    private String createUser;// 创建人账号
    private Date createTime;// 创建时间
    private Date updateTime;// 更新时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public byte getAdvertType() {
        return advertType;
    }

    public void setAdvertType(byte advertType) {
        this.advertType = advertType;
    }

    public String getAdWebUrl() {
        return adWebUrl;
    }

    public void setAdWebUrl(String adWebUrl) {
        this.adWebUrl = adWebUrl;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
