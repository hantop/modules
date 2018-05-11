package com.fenlibao.model.pms.common.publicity;

/**
 * 启动图文件
 * <p>
 * Created by chenzhixuan on 2016/6/01.
 */
public class StartupImageFile {
    private int id;
    private String originalName;// 源文件名称
    private String publishName;// 发布名称
    private byte screenType;// 客户端屏幕分辨率类型
    private long fileSize;// 文件大小
    private String fileType;// 文件类型
    private String serverPath;//文件服务器路径
    private String relativePath;//服务器相对路径
    private String createUser;
    private String fullPath;//全路径

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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}