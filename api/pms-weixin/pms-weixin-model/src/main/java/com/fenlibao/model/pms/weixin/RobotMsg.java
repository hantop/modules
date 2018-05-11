package com.fenlibao.model.pms.weixin;

import java.util.Date;

/**
 * Created by Bogle on 2016/3/4.
 */
public class RobotMsg {
    private Integer id = 0;

    private Date createTime;

    private String msgType;

    private String keyword;

    private String desc;

    private String content;

    private String activeProfiles;

    private Type type;

    public RobotMsg() {
    }

    public RobotMsg(Date createTime, String msgType, String activeProfiles,String content,Type type) {
        this.createTime = createTime;
        this.msgType = msgType;
        this.activeProfiles = activeProfiles;
        this.content = content;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActiveProfiles() {
        return activeProfiles;
    }

    public void setActiveProfiles(String activeProfiles) {
        this.activeProfiles = activeProfiles;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        CONTENT,CLICK
    }
}
