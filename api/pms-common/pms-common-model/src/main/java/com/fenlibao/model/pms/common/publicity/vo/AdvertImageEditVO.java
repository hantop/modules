package com.fenlibao.model.pms.common.publicity.vo;

import com.fenlibao.model.pms.common.publicity.AdvertImageFile;

import java.util.List;

/**
 * 广告图编辑
 * <p>
 * Created by chenzhixuan on 2016/5/24.
 */
public class AdvertImageEditVO {
    private String id;
    private String name;// 名称
    private byte priority;// 优先级
    private byte advertType;// 广告类型(对应枚举key)
    private String adWebUrl;// 广告链接
    private byte needLogin;// 是否需要登陆(缺省0,1:启用,2:禁用)
    private byte status;// 状态(缺省0,1:启用,2:禁用)
    private String createUser;// 创建人账号
    //    private Date createTime;// 创建时间
//    private Date updateTime;// 更新时间
    private List<String> versions;// 版本号列表
    private List<Integer> clientTypes;// 客户端类型列表
    private List<AdvertImageFile> adImgFiles;// 广告图文件列表
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public List<AdvertImageFile> getAdImgFiles() {
        return adImgFiles;
    }

    public void setAdImgFiles(List<AdvertImageFile> adImgFiles) {
        this.adImgFiles = adImgFiles;
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