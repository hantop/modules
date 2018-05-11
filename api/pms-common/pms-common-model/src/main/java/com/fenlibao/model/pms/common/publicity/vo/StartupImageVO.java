package com.fenlibao.model.pms.common.publicity.vo;

import com.fenlibao.model.pms.common.publicity.StartupImageFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 启动图
 * <p>
 * Created by chenzhixuan on 2016/6/1.
 */
public class StartupImageVO {
    private String id;
    private String name;// 名称
    private byte status;// 状态(缺省0,1:启用,2:禁用)
    private String createUser;// 创建人账号
    private Date createTime;// 创建时间
    private Date updateTime;// 更新时间
    private List<Integer> clientTypes;// 客户端类型列表
    private Map<Byte, StartupImageFile> imgFileMap;// 分辨率类型ID以key的启动图文件列表

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

    public List<Integer> getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(List<Integer> clientTypes) {
        this.clientTypes = clientTypes;
    }

    public Map<Byte, StartupImageFile> getImgFileMap() {
        return imgFileMap;
    }

    public void setImgFileMap(Map<Byte, StartupImageFile> imgFileMap) {
        this.imgFileMap = imgFileMap;
    }
}