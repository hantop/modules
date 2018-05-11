package com.fenlibao.model.pms.da.biz.xieyi;

import java.util.Date;

/**
 * Created by zeronx on 2017/8/31.
 */
public class XieyiInfo {
    private int id;
    private int downloadRecordId;
    private String type;
    private Date createTime;
    private Date yesterday;
    private String name;
    private String rrename;
    private String format;
    private int uploadUserId;
    private String pkData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDownloadRecordId() {
        return downloadRecordId;
    }

    public void setDownloadRecordId(int downloadRecordId) {
        this.downloadRecordId = downloadRecordId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getYesterday() {
        return yesterday;
    }

    public void setYesterday(Date yesterday) {
        this.yesterday = yesterday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRrename() {
        return rrename;
    }

    public void setRrename(String rrename) {
        this.rrename = rrename;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(int uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getPkData() {
        return pkData;
    }

    public void setPkData(String pkData) {
        this.pkData = pkData;
    }
}
