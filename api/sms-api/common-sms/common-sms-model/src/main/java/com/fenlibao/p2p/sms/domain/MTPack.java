package com.fenlibao.p2p.sms.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2015/8/28.
 */
public class MTPack implements Serializable {

    private long id;

    private UUID batch;

    private Long createTime;//创建时间
    private String tunnel;//通道业务
    private Long gsmsResponseId;//响应结果信息
    private ServerType serverType;//客服端类型，易美，玄武
    private String desc;

    private UUID batchID;
    private String batchName;

    private MTPack.SendType sendType;
    private MTPack.MsgType msgType;
    private List<MessageData> msgs;
    private List<MediaItem> medias;
    private String customNum;
    private int bizType;
    private String bizCode;//业务编码
    private boolean distinctFlag = false;
    private long scheduleTime = 0;
    private long deadline;
    private String remark;
    private String templateNo;

    private int priority;//优先级

    private String username;
    private String password;
    private int count;

    public MTPack() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime == null ? System.currentTimeMillis() : createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTunnel() {
        return tunnel;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
    }

    public Long getGsmsResponseId() {
        return gsmsResponseId;
    }

    public void setGsmsResponseId(Long gsmsResponseId) {
        this.gsmsResponseId = gsmsResponseId;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UUID getBatchID() {
        return batchID == null ? UUID.randomUUID() : batchID;
    }

    public void setBatchID(UUID batchID) {
        this.batchID = batchID;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public SendType getSendType() {
        return sendType == null ? MTPack.SendType.MASS : sendType;
    }

    public void setSendType(SendType sendType) {
        this.sendType = sendType;
    }

    public MsgType getMsgType() {
        return msgType == null ? MTPack.MsgType.SMS : msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public List<MessageData> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<MessageData> msgs) {
        this.msgs = msgs;
    }

    public List<MediaItem> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaItem> medias) {
        this.medias = medias;
    }

    public String getCustomNum() {
        return customNum;
    }

    public void setCustomNum(String customNum) {
        this.customNum = customNum;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public boolean isDistinctFlag() {
        return distinctFlag;
    }

    public void setDistinctFlag(boolean distinctFlag) {
        this.distinctFlag = distinctFlag;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public UUID getBatch() {
        return batch == null ? UUID.randomUUID() : batch;
    }

    public void setBatch(UUID batch) {
        this.batch = batch;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static enum MsgType {
        SMS(1),
        MMS(2);

        private final int index;

        MsgType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

    public static enum SendType {
        MASS(0),
        GROUP(1);

        private final int index;

        SendType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public static enum ServerType {
        EMAY("易美"), XUANWU_STANDARD("玄武普通短信通道"), XUANWU_MARKET("玄武营销通道"),ALIDAYU("阿里大鱼");
        private String name;

        ServerType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum TunnelType {
        MARKET, STANDARD;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }
}
