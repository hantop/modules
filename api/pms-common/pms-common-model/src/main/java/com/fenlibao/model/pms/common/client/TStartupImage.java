package com.fenlibao.model.pms.common.client;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户端启动页图片
 * Created by Lullaby on 2015/7/27.
 */
public class TStartupImage implements Serializable {

    private int id;//主键

    private String originalName;//源名称

    private String publishName;//发布名称

    private String serverPath;//文件服务器路径

    private String relativePath;//服务器相对路径

    private byte clientType;//客户端类型(缺省0,1:iOS,2:Android)

    private byte screenType;//屏幕分辨率类型

    private long fileSize;//文件大小

    private String fileType;//文件类型

    private byte status;//状态

    private String createUser;//创建人账号

    private Date createTime;//创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public byte getClientType() {
        return clientType;
    }

    public void setClientType(byte clientType) {
        this.clientType = clientType;
    }

    public byte getScreenType() {
        return screenType;
    }

    public void setScreenType(byte screenType) {
        this.screenType = screenType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

}
