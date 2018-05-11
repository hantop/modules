package com.fenlibao.model.pms.common.publicity.vo;

import com.fenlibao.model.pms.common.global.SystemType;
import com.fenlibao.model.pms.common.publicity.AdvertImageFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 广告图
 *
 * Created by chenzhixuan on 2016/5/11.
 */
public class AdvertImageVO {
    private String id;
    private String name;// 名称
    private String priority;// 优先级
    private String advertType;// 广告类型(对应枚举key)
    private String adWebUrl;// 广告链接
    private byte needLogin;// 是否需要登陆(缺省0,1:启用,2:禁用)
    private byte status;// 状态(缺省0,1:启用,2:禁用)
    private String createUser;// 创建人账号
    private Date createTime;// 创建时间
    private Date updateTime;// 更新时间
    private List<String> versions;// 版本号列表
    private List<Integer> clientTypes;// 客户端类型列表
    private SystemType[] systemTypes;// 系统类型
    private Map<Byte, AdvertImageFile> adImgFileMap;// 分辨率类型ID以key的广告图文件列表
    private int systemType;
    private String systemTypeLabel;

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
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

    public Map<Byte, AdvertImageFile> getAdImgFileMap() {
        return adImgFileMap;
    }

    public void setAdImgFileMap(Map<Byte, AdvertImageFile> adImgFileMap) {
        this.adImgFileMap = adImgFileMap;
    }

    public int getSystemType() {
        return systemType;
    }

    public void setSystemType(int systemType) {
        this.systemType = systemType;
    }

    public String getSystemTypeLabel() {
        return systemTypeLabel;
    }

    public void setSystemTypeLabel(String systemTypeLabel) {
        this.systemTypeLabel = systemTypeLabel;
    }

    public SystemType[] getSystemTypes() {
        return systemTypes;
    }

    public void setSystemTypes(SystemType[] systemTypes) {
        this.systemTypes = systemTypes;
    }
}
