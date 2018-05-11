package com.fenlibao.model.pms.common.publicity.form;

import java.util.List;

/**
 * 广告图编辑
 * <p>
 * Created by chenzhixuan on 2016/5/11.
 */
public class AdvertImageEditForm {
    private String id;
    private String name;// 名称
    private byte priority;// 优先级
    private byte advertType;// 广告类型(对应枚举key)
    private String adWebUrl;// 广告链接
    private byte needLogin;// 是否需要登陆(缺省0,1:启用,2:禁用)
    private byte status;// 状态(缺省0,1:启用,2:禁用)
    //    private Date createTime;// 创建时间
//    private Date updateTime;// 更新时间
    private List<String> versions;// 版本号列表
    private List<Integer> clientTypes;// 客户端类型列表
    //    private List<AdvertImageFile> adImgFiles;// 广告图文件列表
    private List<String> publishNames;// 发布名称
    private List<String> originalNames;// 源文件名称
    private List<Byte> screenTypes;// 客户端屏幕分辨率类型
    private List<Long> fileSizes;// 文件大小
    private List<String> fileTypes;// 文件类型
    private List<String> relativePaths;// 相对路径
    private int systemType;// 系统类型

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public byte getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(byte needLogin) {
        this.needLogin = needLogin;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public List<Integer> getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(List<Integer> clientTypes) {
        this.clientTypes = clientTypes;
    }

    public List<String> getPublishNames() {
        return publishNames;
    }

    public void setPublishNames(List<String> publishNames) {
        this.publishNames = publishNames;
    }

    public List<String> getOriginalNames() {
        return originalNames;
    }

    public void setOriginalNames(List<String> originalNames) {
        this.originalNames = originalNames;
    }

    public List<Byte> getScreenTypes() {
        return screenTypes;
    }

    public void setScreenTypes(List<Byte> screenTypes) {
        this.screenTypes = screenTypes;
    }

    public List<Long> getFileSizes() {
        return fileSizes;
    }

    public void setFileSizes(List<Long> fileSizes) {
        this.fileSizes = fileSizes;
    }

    public List<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public List<String> getRelativePaths() {
        return relativePaths;
    }

    public void setRelativePaths(List<String> relativePaths) {
        this.relativePaths = relativePaths;
    }

    public int getSystemType() {
        return systemType;
    }

    public void setSystemType(int systemType) {
        this.systemType = systemType;
    }
}