package com.fenlibao.p2p.sms.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/8/28.
 */
public class MessageData implements Serializable {

    @JSONField(serialize=false)
    private Long id;
    @JSONField(serialize=false)
    private Long createTime;//创建时间
    @JSONField(serialize=false)
    private Long mtPackId;

    private String phone;
    private String content;
    @JSONField(serialize=false)
    private List<MediaItem> medias;
    private String customMsgID;
    @JSONField(serialize=false)
    private String customNum;
    @JSONField(serialize=false)
    private boolean vipFlag = false;

    public MessageData() {
    }

    public MessageData(String phone, String content) {
        this.phone = phone;
        this.content = content;
    }

    public MessageData(String phone, String content, String customMsgID) {
        this.phone = phone;
        this.content = content;
        this.customMsgID = customMsgID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getMtPackId() {
        return mtPackId;
    }

    public void setMtPackId(Long mtPackId) {
        this.mtPackId = mtPackId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MediaItem> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaItem> medias) {
        this.medias = medias;
    }

    public String getCustomMsgID() {
        return customMsgID;
    }

    public void setCustomMsgID(String customMsgID) {
        this.customMsgID = customMsgID;
    }

    public String getCustomNum() {
        return customNum;
    }

    public void setCustomNum(String customNum) {
        this.customNum = customNum;
    }

    public boolean isVipFlag() {
        return vipFlag;
    }

    public void setVipFlag(boolean vipFlag) {
        this.vipFlag = vipFlag;
    }
}
